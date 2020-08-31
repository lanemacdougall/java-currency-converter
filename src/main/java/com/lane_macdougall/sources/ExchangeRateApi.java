package com.lane_macdougall.sources;

// For exchange rates between base currency and all supported currencies:
// GET https://v6.exchangerate-api.com/v6/YOUR-API-KEY/latest/BASE-CURRENCY

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lane_macdougall.api_keys.ApiKey;
import com.lane_macdougall.responses.ExchangeRateApiResponse;
import com.lane_macdougall.utility.ApiRequestUtility;

import java.io.IOException;


/* PURPOSE: Used to retrieve the exchange rates for a given base currency using the Exchange Rate API.
 *
 * DEPENDENCY: fasterxml.jackson.databind.ObjectMapper.
 */
public class ExchangeRateApi {

    /* Methods */

    /* Method retrieves and parses the JSON object returned by the API using the ApiRequestUtility class'
     * retrieveCurrencyInfo() method, the Jackson (databind) ObjectMapper class, and the ExchangeRateApiResponse class.
     *
     * Method returns a hash map containing currencies (keys) and their exchange rates (values)
     */
    public ExchangeRateApiResponse requestRates(ApiKey apiKey, String from) throws IOException {

        // Retrieve response from API using the ApiRequestUtility class' retrieveJSON method
        String apiResponse = ApiRequestUtility.retrieveJSON(formUrlString(apiKey.getExchangeRateAPIKey(), from), true);

        /* Replace the "error-type" JSON key (if present) with the key "error_type" because "error-type" is an
         * invalid Java class attribute name and the JSON key name needs to match the name of the object that the JSON
         * object is mapped to using ObjectMapper.
         */
        apiResponse = apiResponse.replace("error-type", "error_type");

        ObjectMapper objMapper = new ObjectMapper();

        // Convert JSON object String into an ExchangeRateApiResponse object
        ExchangeRateApiResponse response = objMapper.readValue(apiResponse, ExchangeRateApiResponse.class);

        return response;
    }

    // Form the API server URL using the specified API key and base currency code
    private static String formUrlString(String apiKey, String from) {
        return "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + from;
    }

}
