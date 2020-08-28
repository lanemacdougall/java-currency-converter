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

    public static Double retrieveRates(ApiKey key, String baseCurr, String to) {
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

        } else if (key.getCurrencyLayerAPIKey() != null){
            CurrencyLayerApi currencyLayerApi = new CurrencyLayerApi();
            Map<String, Double> exchangeRates = new HashMap<>();
            try {
                exchangeRates = currencyLayerApi.requestRates(key.getCurrencyLayerAPIKey(), baseCurr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Retrieve specific rate from Map of exchange rates (if rate is available)
            if (!exchangeRates.containsKey(baseCurr + to)) {
                throw new CurrencyConverterException("Requested conversion currency is not available.");
            }

            return exchangeRates.get(baseCurr + to);

        }

        // If the method reaches this point, none of the APIs are available
        throw new CurrencyConverterException("No service available. Try again later.");
    }
}
