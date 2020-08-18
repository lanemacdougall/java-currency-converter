package com.lane_macdougall;

// For exchange rates between base currency and all supported currencies:
// GET https://v6.exchangerate-api.com/v6/YOUR-API-KEY/latest/BASE-CURRENCY

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;


/* PURPOSE: Used to retrieve the exchange rates for a given base currency using the Exchange Rate API.
 *
 * DEPENDENCY: fasterxml.jackson.databind.ObjectMapper.
 */
public class ExchangeRateApi {
    private static String EXCHANGE_RATE_API_KEY;

    /* Methods */

    /* Method retrieves and parses the JSON object returned by the API using the ApiRequestUtility class'
     * retrieveCurrencyInfo() method, the Jackson (databind) ObjectMapper class, and the ExchangeRateApiResponse class.
     *
     * Method saves a hash map containing currencies and their exchange rates to the ExchangeRateApi object's
     * allExchangeRates attribute.
     */
    public Map<String, Double> requestRates(String apiKey, String from) throws IOException {
        String apiResponse = ApiRequestUtility.retrieveCurrencyInfo(formUrlString(apiKey, from), true);

        /* Replace the "error-type" JSON key (if present) with the key "error_type" because "error-type" is an
         * invalid Java class attribute name and the JSON key name needs to match the name of the object that the JSON
         * object is mapped to using ObjectMapper.
         */
        apiResponse = apiResponse.replace("error-type", "error_type");

        ObjectMapper objMapper = new ObjectMapper();

        // Convert JSON object String into an ExchangeRateApiResponse object
        ExchangeRateApiResponse response = objMapper.readValue(apiResponse, ExchangeRateApiResponse.class);

        // If the request was not successfully served, throw an exception according to the error type
        if (!response.getResult().equals("success")) {
            switch (response.getError_type()) {
                case "unsupported-code":
                    throw new CurrencyConverterException("Unsupported currency code entered.");
                case "base-code-only-on-pro":
                    throw new CurrencyConverterException("You have submitted a base code other than USD or EUR.");
                case "malformed-request":
                    throw new CurrencyConverterException("Request is not properly formatted.");
                case "invalid-key":
                    throw new CurrencyConverterException("Invalid API key.");
                case "quota-reached":
                    throw new CurrencyConverterException("Request quota exceeded.");
                case "not-available-on-plan":
                    throw new CurrencyConverterException("Your plan level doesn't support this type of request or endpoint.");
                default:
                    throw new CurrencyConverterException("An unknown error occurred.");
            }
        }

        // If the response's base code does not match the specified base code, throw an exception
        String baseCurr = response.getBase_code();
        if (!from.equals(baseCurr)) {
            throw new CurrencyConverterException("Incorrect currency retrieved.");
        }

        return response.getConversion_rates();
    }

    // Form the API server URL using the specified API key and base currency code
    public String formUrlString(String apiKey, String from){
        return "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + from;
    }

}
