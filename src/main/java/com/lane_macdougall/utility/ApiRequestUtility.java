package com.lane_macdougall.utility;

import com.lane_macdougall.exception.CurrencyConverterException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* PURPOSE: Send an HTTP(S) GET request to the specified API URL and return the server's JSON object response (in String
 * form).
 *
 * retrieveCurrencyInfo() method is designed to work with any of the supported APIs and with both HTTP and HTTPS
 * protocols.
 */
public class ApiRequestUtility {

    public static int requestCount = 0;

    public static int getRequestCount(){ return requestCount; }

    public static void setRequestCount(int count){ requestCount = count; }

    public static String retrieveJSON(String urlStr, boolean httpsProtocol) throws IOException {

        // Use urlStr argument to instantiate a URL object
        URL url = new URL(urlStr);

        HttpURLConnection request;
        // If the URL is HTTPS, cast the URLConnection returned by openConnection() into an HttpsURLConnection object
        if (httpsProtocol) {
            request = (HttpsURLConnection) url.openConnection();
        }
        // Otherwise, cast the URLConnection into an HttpURLConnection
        else {
            request = (HttpURLConnection) url.openConnection();
        }

        request.setRequestMethod("GET");
        request.setRequestProperty("Accept", "application/json");

        // If request is not successful (response code 200, "OK"), print error and exit session
        if (request.getResponseCode() != 200) {
            // TODO: If appropriate, try using other API
            // NOTE: Difficulty with the above ^ is that both service classes use this class
            throw new CurrencyConverterException("Error: Response Code: " + request.getResponseCode());
        }

        /* Read the Http(s)URLConnection's input stream using a BufferReader
         * The Stream's collect() method (with the Collectors.joining() method as an argument) concatenates the input
         * elements into a String in the order in which they are read from the Stream
         */
        BufferedReader responseBufferReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        Stream<String> responseStream = responseBufferReader.lines();
        // response contains the currency data
        String response = responseStream.collect(Collectors.joining());

        // Terminate request with server
        request.disconnect();

        requestCount++;

        return response;

    }

}
