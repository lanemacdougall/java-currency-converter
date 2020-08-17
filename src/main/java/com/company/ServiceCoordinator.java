package com.company;

import java.util.Map;

/* PURPOSE: Find an available API service and return a hash map containing currencies and exchange rates provided by
 * that service.
 */
public class ServiceCoordinator {
    public static Map<String, Double> retrieveRates(ApiKey key, String baseCurr) {
        if (key.getExchangeRateAPIKey() != null){
            ExchangeRateApi exchangeRateApi = new ExchangeRateApi();
            try {
                return exchangeRateApi.requestRates(key.getExchangeRateAPIKey(), baseCurr);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        throw new CurrencyConverterException("Error: No service available. Try again later.");
    }
}
