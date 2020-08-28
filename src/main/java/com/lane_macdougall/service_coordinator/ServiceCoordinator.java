package com.lane_macdougall.service_coordinator;

import com.lane_macdougall.api_keys.ApiKey;
import com.lane_macdougall.exception.CurrencyConverterException;
import com.lane_macdougall.sources.CurrencyLayerApi;
import com.lane_macdougall.sources.ExchangeRateApi;

import java.util.HashMap;
import java.util.Map;

/* PURPOSE: Find an available API service and return a hash map containing currencies and exchange rates provided by
 * that service.
 */
public class ServiceCoordinator {

    // Once exchange rates have been requested and retrieved, the hash map is stored in the exchangeRates param
    public static Map<String, Double> exchangeRates = new HashMap<>();

    /* Value of serviceSource indicates which API is serving the program
     * -1 = None
     *  0 = Exchange Rate API
     *  1 = currencylayer API
     */
    public static int serviceSource = -1;

    public static Double retrieveRates(ApiKey key, String baseCurr, String to) {
        // Key used to retrieve rate from exchangeRates hash map
        String retrieveCode = new String();

        /* If the exchange rates have already been requested and retrieved, do not send another request;
         * retrieve the value (if available) from the existing hash map.
         *
         * Else, request and retrieve the exchange rates from the first API source for which the access key
         * is supplied.
         */
        if (!exchangeRates.isEmpty()){
            if (!exchangeRates.containsKey(to)) {
                throw new CurrencyConverterException("Requested conversion currency is not available.");
            }
            if (serviceSource == 0){
                retrieveCode = to;
            } else if (serviceSource == 1) {
                retrieveCode = baseCurr + to;
            }
        } else if (key.getExchangeRateAPIKey() != null) {
            ExchangeRateApi exchangeRateApi = new ExchangeRateApi();
            try {
                exchangeRates = exchangeRateApi.requestRates(key.getExchangeRateAPIKey(), baseCurr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Retrieve specific rate from Map of exchange rates (if rate is available)
            if (!exchangeRates.containsKey(to)) {
                throw new CurrencyConverterException("Requested conversion currency is not available.");
            }

            serviceSource = 0;
            retrieveCode = to;

        } else if (key.getCurrencyLayerAPIKey() != null){
            CurrencyLayerApi currencyLayerApi = new CurrencyLayerApi();
            try {
                exchangeRates = currencyLayerApi.requestRates(key.getCurrencyLayerAPIKey(), baseCurr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Retrieve specific rate from Map of exchange rates (if rate is available)
            if (!exchangeRates.containsKey(baseCurr + to)) {
                throw new CurrencyConverterException("Requested conversion currency is not available.");
            }

            serviceSource = 1;
            retrieveCode = baseCurr + to;

        } else {
            // If the method reaches this point, none of the APIs are available
            throw new CurrencyConverterException("No service available. Try again later.");
        }

        // Return the specified rate
        return exchangeRates.get(retrieveCode);

    }
}
