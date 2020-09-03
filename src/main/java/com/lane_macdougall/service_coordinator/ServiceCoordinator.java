package com.lane_macdougall.service_coordinator;

import com.lane_macdougall.api_keys.ApiKey;
import com.lane_macdougall.exception.CurrencyConverterException;
import com.lane_macdougall.responses.CurrencyLayerApiResponse;
import com.lane_macdougall.responses.ExchangeRateApiResponse;
import com.lane_macdougall.sources.CurrencyLayerApi;
import com.lane_macdougall.sources.ExchangeRateApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* PURPOSE: Find an available API service and return a hash map containing currencies and exchange rates provided by
 * that service.
 */
public class ServiceCoordinator {

    private static Logger log = Logger.getLogger(ServiceCoordinator.class.getName());

    // Number of APIs servicing exchange rate requests from applications
    private static final int NUM_API = 2;

    // Boolean value indicating whether or not exchange rates were retrieved successfully
    private static boolean successfulRetrieval = false;

    // Value that indicates which of the APIs a previously successful retrieval was carried-out through
    private static int source = -1;

    // Once exchange rates have been requested and retrieved, the hash map is stored in the exchangeRates param
    private static Map<String, Double> exchangeRates = new HashMap<>();

    // Requested exchange rate
    private static Double exchangeRate;


    /* PURPOSE: Method sends requests for exchange rates to supported APIs.
     *
     * RETURNS: Exchange rate between the specified base currency and conversion currency.
     */
    public static Double retrieveRates(ApiKey key, String baseCurr, String to) throws IOException {
        /* If the exchange rates have already been requested and retrieved, do not send another request;
         * retrieve the value (if available) from the existing hash map.
         *
         * Else, request and retrieve the exchange rates from the first API source for which the access key
         * is supplied.
         */
        if (successfulRetrieval && (exchangeRates.containsKey(to) || exchangeRates.containsKey(baseCurr+to))){
            /* Currency codes in Exchange Rate API responses are just the conversion currencies' ISO 4217 three-letter codes, while
             * the currency codes in currencylayer API responses are the ISO 4217 three-letter codes of both the base currency
             * and the conversion currency concatenated. Separating the exchange rate retrieval from a saved hash map is
             * necessary for this reason.
             */
            if (source == 0){
                exchangeRate = exchangeRates.get(to);
            } else if (source == 1){
                exchangeRate = exchangeRates.get(baseCurr+to);
            }
        } else {
            int i = 0;
            /* While there are API sources that have not been tried and there has not been a successful retrieval,
             * send a request to the next supported API.
             */
            while (i <= NUM_API & !successfulRetrieval){
                switch (i){
                    case 0:
                        exchangeRateApiRequest(key, baseCurr, to);
                        break;
                    case 1:
                        currencyLayerApiRequest(key, baseCurr, to);
                        break;
                    default:
                        throw new CurrencyConverterException("No service available. Try again later.");
                }
                i++;
            }
        }

        // Return the specified rate
        return exchangeRate;
    }


    /* PURPOSE: Method sends a request to Exchange Rate API and, if the request is successful and the desired rate
     * is contained in the response, retrieve the specified exchange rate.
     */
    private static void exchangeRateApiRequest(ApiKey key, String from, String to) throws IOException {
        // Make sure the API access key has been provided in the CurrencyConverter object's constructor
        if (key.getExchangeRateAPIKey() != null) {
            ExchangeRateApi exchangeRateApi = new ExchangeRateApi();

            // Send request to API
            ExchangeRateApiResponse response = exchangeRateApi.requestRates(key, from);

            if (response.getResult()
                    .equals("success")) {
                Map<String, Double> rates = response.getConversion_rates();
                // Retrieve specific rate from Map of exchange rates (if rate is available)
                if (rates.containsKey(to)) {
                    // Save the returned exchange rates hash map
                    exchangeRates = rates;
                    // Retrieve the specified exchange rate
                    exchangeRate = exchangeRates.get(to);
                    // Indicate the retrieval has been successful
                    successfulRetrieval = true;
                    // Indicate which API delivered response - used in retrieving values from saved hash map
                    source = 0;
                }
            } else {
                handleExchangeRateApiError(response);
            }
        }
    }


    /* PURPOSE: Method sends a request to currencylayer API and, if the request is successful and the desired rate
     * is contained in the response, retrieve the specified exchange rate.
     */
    private static void currencyLayerApiRequest(ApiKey key, String from, String to) throws IOException {
        // Make sure the API access key has been provided in the CurrencyConverter object's constructor
        if (key.getCurrencyLayerAPIKey() != null) {
            CurrencyLayerApi currencyLayerApi = new CurrencyLayerApi();

            // Send request to API
            CurrencyLayerApiResponse response = currencyLayerApi.requestRates(key, from);

            if (response.isSuccess()) {
                Map<String, Double> rates = response.getQuotes();
                // Retrieve specific rate from Map of exchange rates (if rate is available)
                if (rates.containsKey(from + to)) {
                    // Save the returned exchange rates hash map
                    exchangeRates = rates;
                    // Retrieve the specified exchange rate
                    exchangeRate = exchangeRates.get(from + to);
                    // Indicate the retrieval has been successful
                    successfulRetrieval = true;
                    // Indicate which API delivered response - used in retrieving values from saved hash map
                    source = 1;
                }
            } else {
                handleCurrencyLayerApiError(response);
            }
        }
    }


    /* PURPOSE: Method logs any API server-side errors encountered in attempting to retrieve exchange rates from the
     * API. This approach was taken, as opposed to throwing CurrencyConverterExceptions, because if an error is
     * encountered while sending a request to one API, the system will try again with the remaining APIs utilized by
     * the library.
     */
    /* NOTE: I am not confident in the practicality of this approach. Please feel free to send a message with
     *  suggestions for improvement or to submit a pull request.
     */
    private static void handleExchangeRateApiError(ExchangeRateApiResponse response){
        log.setLevel(Level.INFO);
        // If the request was not successfully served, log the error
        switch (response.getError_type()) {
            case "unsupported-code":
                log.warning("Exchange Rate API: Unsupported currency code entered.");
                break;
            case "base-code-only-on-pro":
                log.warning("Exchange Rate API: You have submitted a base code other than USD or EUR.");
                break;
            case "malformed-request":
                log.warning("Exchange Rate API: Request is not properly formatted.");
                throw new CurrencyConverterException("Exchange Rate API: Request is not properly formatted.");
            case "invalid-key":
                log.warning("Exchange Rate API: Invalid API key.");
                break;
            case "quota-reached":
                log.warning("Exchange Rate API: Request quota exceeded.");
                break;
            case "not-available-on-plan":
                log.warning("Exchange Rate API: Your plan level doesn't support this type of request or endpoint.");
                break;
            default:
                log.warning("Exchange Rate API: An unknown error occurred with the Exchange Rate API.");
        }
    }


    /* PURPOSE: Method logs any API server-side errors encountered in attempting to retrieve exchange rates from the
     * API. This approach was taken, as opposed to throwing CurrencyConverterExceptions, because if an error is
     * encountered while sending a request to one API, the system will try again with the remaining APIs utilized by
     * the library.
     */
    private static void handleCurrencyLayerApiError(CurrencyLayerApiResponse response){
        log.setLevel(Level.INFO);
        /* If the request was not successful, log the error code and message from the
         * server's response.
         */
        int errorCode = (int) response.getError().get("code");
        String errorMsg = (String) response.getError().get("info");
        log.warning("currencylayer API: Error Code: " + errorCode + "\n" + errorMsg);

    }


}
