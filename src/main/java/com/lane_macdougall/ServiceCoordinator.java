package com.lane_macdougall;

import java.util.HashMap;
import java.util.Map;

/* PURPOSE: Find an available API service and return a hash map containing currencies and exchange rates provided by
 * that service.
 */
// NOTE: This class will be much more important when additional APIs are supported by the library
public class ServiceCoordinator {

    public static Double retrieveRates(ApiKey key, String baseCurr, String to) {
        // PENDING: As more APIs are supported, they will be added to this process (most efficient APIs first)
        if (key.getExchangeRateAPIKey() != null) {
            ExchangeRateApi exchangeRateApi = new ExchangeRateApi();
            Map<String, Double> exchangeRates = new HashMap<>();
            try {
                exchangeRates = exchangeRateApi.requestRates(key.getExchangeRateAPIKey(), baseCurr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Retrieve specific rate from Map of exchange rates (if rate is available)
            if (!exchangeRates.containsKey(to)) {
                throw new CurrencyConverterException("Requested conversion currency is not available.");
            }
            return exchangeRates.get(to);
        }
        // If the method reaches this point, none of the APIs are available
        throw new CurrencyConverterException("No service available. Try again later.");
    }
}
