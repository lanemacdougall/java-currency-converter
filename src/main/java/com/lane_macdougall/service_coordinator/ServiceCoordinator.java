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

    private static String retrieveCode;

    // Once exchange rates have been requested and retrieved, the hash map is stored in the exchangeRates param
    private static Map<String, Double> exchangeRates = new HashMap<>();

    // Requested exchange rate
    private static Double exchangeRate;


    public static Double retrieveRates(ApiKey key, String baseCurr, String to) throws IOException {
        /* If the exchange rates have already been requested and retrieved, do not send another request;
         * retrieve the value (if available) from the existing hash map.
         *
         * Else, request and retrieve the exchange rates from the first API source for which the access key
         * is supplied.
         */
        if (successfulRetrieval && exchangeRates != null && exchangeRates.containsKey(retrieveCode)) {
            exchangeRate = exchangeRates.get(retrieveCode);
        } else {
            int i = 0;
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


    private static void exchangeRateApiRequest(ApiKey key, String from, String to) throws IOException {
        if (key.getExchangeRateAPIKey() != null) {
            ExchangeRateApi exchangeRateApi = new ExchangeRateApi();

            ExchangeRateApiResponse response = exchangeRateApi.requestRates(key, from);

            if (response.getResult()
                    .equals("success")) {
                Map<String, Double> rates = response.getConversion_rates();
                // Retrieve specific rate from Map of exchange rates (if rate is available)
                if (rates.containsKey(to)) {
                    exchangeRates = rates;
                    exchangeRate = exchangeRates.get(to);
                    successfulRetrieval = true;
                    retrieveCode = to;
                }
            } else {
                handleExchangeRateApiError(response);
            }
        }
    }

    private static void currencyLayerApiRequest(ApiKey key, String from, String to) throws IOException {
        if (key.getCurrencyLayerAPIKey() != null) {
            CurrencyLayerApi currencyLayerApi = new CurrencyLayerApi();

            CurrencyLayerApiResponse response = currencyLayerApi.requestRates(key, from);

            if (response.isSuccess()) {
                Map<String, Double> rates = response.getQuotes();
                // Retrieve specific rate from Map of exchange rates (if rate is available)
                if (rates.containsKey(from + to)) {
                    exchangeRates = rates;
                    exchangeRate = exchangeRates.get(from + to);
                    successfulRetrieval = true;
                    retrieveCode = from + to;
                }
            } else {
                handleCurrencyLayerApiError(response);
            }
        }
    }


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
