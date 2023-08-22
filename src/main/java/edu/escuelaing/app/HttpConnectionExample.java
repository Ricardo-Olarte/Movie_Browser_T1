package edu.escuelaing.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/*
* Code the professor class
* It´s a connection HTTP
* Has a API and URL, construct them
* @author Luis Benavides
* */
public class HttpConnectionExample {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://www.omdbapi.com";
    private static final String API_KEY = "&apikey=e0993287";
    private static final HashMap<String,String> CACHE = new HashMap<>();

    /**
     * Build a URL with the API for get information about the movie
     * @param movie
     * @return String, expectedResponds, it´s a JSON string
     * @throws IOException
     */
    public String service(String movie) throws IOException {

        if(CACHE.containsKey(movie)){
            return CACHE.get(movie);
        }
        String expectedResponds = "";
        URL obj = new URL(GET_URL + movie.replace(" ","+") + API_KEY);
        //System.out.println(obj.toString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            //System.out.println(response.toString());

            expectedResponds = response.toString();

        } else {
            expectedResponds = "The server its failed";
            System.out.println(expectedResponds);
        }
        System.out.println("GET DONE");
        CACHE.put(movie,expectedResponds);
        //System.out.println(CACHE);
        // System.out.println(expectedResponds);
        return expectedResponds;
    }
}
