package edu.escuelaing.app;

import java.net.*;
import java.io.*;

/*
* It's http server, where connect in the localhost since your browser
* @author Luis Benavides
* */
public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(42800);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 42800.");
            System.exit(1);
        }
        boolean running = true;
        while(running) {
            Socket clientSocket = null;
            try {
                System.out.println("Ready to receive ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            String uriString = "";

            boolean firstLine = true;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if(firstLine){
                    firstLine = false;
                    uriString = inputLine.split(" ")[1];
                }
                if (!in.ready()) {
                    break;
                }
            }
            //System.out.println("URI: " + uriString);
            if(uriString.startsWith("/?t=")){
                outputLine = getHello(uriString);
            }else{
                outputLine = indexResponse();
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Get information about the movie with a JSON string
     * @param uri
     * @return String uri
     */
    public static String getHello(String uri){
        HttpConnectionExample movie = new HttpConnectionExample();
        String htmlFirst = "HTTP/1.1 200 OK\r\n" + "Content-type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>\n" + "<html>\n" + "<div>\n" + "<p>\n";
        String htmlSecond = "</p>\n" + "</div>\n" + "</html>";
        try {
            String json = movie.service(uri).replace("{", "").replace("}", "");
            String[] infoMovie = json.split(",");
            String htmlBody = "";
            //System.out.println(uri);
            //System.out.println(movie.service(uri));
            //System.out.println(json);
            for(String keyInfo : infoMovie){
                htmlBody += keyInfo.replace("\"","") + "<br/>";
            }
            //System.out.println(htmlBody);
            return htmlFirst + htmlBody + htmlSecond;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * It's HTML string, use to view for the user
     * @return
     */
    public static  String indexResponse(){
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>T1</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <style>\n"
                + "    body {background-color: black;color: white;display:flex;justify-content:center;flex-direction:column;align-items:center}"
                + "    .input-button{"
                + "    background-color: white;padding:4px 6px;border-radius:8px}"
                + "    #getrespmsg{max-width:768px;margin:0 auto}"
                + "    </style>\n"
                + "    <body>\n"
                + "        <h1><center>Name of the movie</center></h1>\n"
                + "        <form action=\"/hello\">\n"
                + "            <label for=\"name\">Name:</label><br>\n"
                + "            <input type=\"text\" id=\"name\" name=\"name\" value=\"\"><br><br>\n"
                + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\" class=\"input-button\">\n"
                + "        </form> \n"
                + "        <div id=\"getrespmsg\"></div>\n"
                + "\n"
                + "        <script>\n"
                + "            function loadGetMsg() {\n"
                + "                let nameVar = document.getElementById(\"name\").value;\n"
                + "                const xhttp = new XMLHttpRequest();\n"
                + "                xhttp.onload = function() {\n"
                + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
                + "                    this.responseText;\n"
                + "                }\n" + "                xhttp.open(\"GET\", \"/?t=\"+nameVar);\n"
                + "                xhttp.send();\n"
                + "            }\n"
                + "        </script>\n"
                + "\n"
                + "    </body>\n"
                + "</html>";
        return response;
    }
}