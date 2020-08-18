package com.lane_macdougall;

import java.util.Map;

/* PURPOSE: Find an available API service and return a hash map containing currencies and exchange rates provided by
 * that service.
 */
// NOTE: This class will be much more important when additional APIs are supported by the library
public class ServiceCoordinator {

    public static Map<String, Double> retrieveRates(ApiKey key, String baseCurr) {
        // PENDING: As more APIs are supported, they will be added to this process (most efficient APIs first)
        if (key.getExchangeRateAPIKey() != null){
            ExchangeRateApi exchangeRateApi = new ExchangeRateApi();
            try {
                return exchangeRateApi.requestRates(key.getExchangeRateAPIKey(), baseCurr);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        // If the method reaches this point, none of the APIs are available
        throw new CurrencyConverterException("No service available. Try again later.");
    }
}
