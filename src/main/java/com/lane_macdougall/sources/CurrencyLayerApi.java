package com.lane_macdougall.sources;

// For exchange rates between base currency and all supported currencies:
// GET https://api.currencylayer.com/live?access_key=[YOUR_ACCESS_KEY]&source=[FROM_CURRENCY]

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lane_macdougall.api_keys.ApiKey;
import com.lane_macdougall.responses.CurrencyLayerApiResponse;
import com.lane_macdougall.utility.ApiRequestUtility;

import java.io.IOException;


/* PURPOSE: Used to retrieve the exchange rates for a given base currency using the currencylayer API.
 *
 * DEPENDENCY: fasterxml.jackson.databind.ObjectMapper.
 */
public class CurrencyLayerApi {

    /* Methods */

    /* Method retrieves and parses the JSON object returned by the API using the ApiRequestUtility class'
     * retrieveCurrencyInfo() method, the Jackson (databind) ObjectMapper class, and the CurrencyLayerApiResponse class.
     *
     * Method returns a hash map containing currencies (keys) and their exchange rates (values)
     */
    public CurrencyLayerApiResponse requestRates(ApiKey apiKey, String from) throws IOException {

        // Retrieve response from API using the ApiRequestUtility class' retrieveJSON method
        String apiResponse = ApiRequestUtility.retrieveJSON(formUrlString(apiKey.getCurrencyLayerAPIKey(), from), false);

        ObjectMapper objMapper = new ObjectMapper();

        // Convert JSON object String into an CurrencyLayerApiResponse object
        CurrencyLayerApiResponse response = objMapper.readValue(apiResponse, CurrencyLayerApiResponse.class);

        return response;

    }



    private static String formUrlString(String apiKey, String from){
        return "http://api.currencylayer.com/live?access_key=" + apiKey + "&source=" + from;
    }
}
