package com.caiusf.ratemydriving.utils.location;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility class for handling HTTP requests
 *
 * Adapted from: https://android--examples.blogspot.ro/2015/05/how-to-parse-json-data-in-android.html
 *
 * @author cfsuman, 2015
 */
public class HTTPDataHandler {

    /**
     * The stream of characters
     */
    static String stream = null;

    /**
     * Get data over HTTP protocol
     *
     * @param urlString
     *              the url to fetch data from
     *
     * @return  a string containing the data
     */
    public static String getHTTPData(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            System.out.println("URL STRING "  + urlString);


            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
                System.out.println("200 in if!");
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                    System.out.println("LINE " + line);
                }
                stream = sb.toString();
                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            }
            else
            {
                // Do somethingSystem.out.println("500 in else!");
            }
        }catch (MalformedURLException e){
        }catch(IOException e){
            e.printStackTrace();
            stream = "--";
        }
        finally {
        }
        // Return the data from specified url
        return stream;
    }
}