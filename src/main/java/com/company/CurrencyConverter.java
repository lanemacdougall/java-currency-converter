package com.company;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

// Exchange Rate API Access Key: f92a462856fc8ef3d4db524e

/* PURPOSE: Retrieve the supported exchange rates and then retrieve specific rates or convert money amounts
 *
 */
public class CurrencyConverter {

    public ApiKey key;

    // ISO 4217 three-letter code of base currency
    public String baseCurr;

    public Map<String, Double> allExchangeRates;

    public String getBaseCurr() { return baseCurr; }

    // Provide base currency's ISO 4217 three-letter code
    public void setBaseCurr(String baseCurr) { this.baseCurr = baseCurr; }

    // Returns hash map of all supported currencies and their exchange rates
    public Map<String, Double> getAllExchangeRates() { return allExchangeRates; }


    /* Constructor */
    public CurrencyConverter(ApiKey key){ this.key = key; }

    // Retrieve all exchange rates for the specified base currency using the supported APIs
    public Map<String, Double> rates() { return ServiceCoordinator.retrieveRates(this.key, this.baseCurr); }

    // Retrieve a specific exchange rate (using currency's ISO 4217 three-letter code)
    public Double getExchangeRate(String to){
        // If the exchange rates have been retrieved and the specified currency is supported, return its exchange rate
        if (allExchangeRates != null) {
            if (!this.allExchangeRates.containsKey(to)) {
                throw new CurrencyConverterException("Requested conversion currency is not available.");
            }
            return this.allExchangeRates.get(to);
        }
        throw new CurrencyConverterException("Retrieve all rates using rates() method before retrieving a specific exchange rate.");
    }

    // Convert an amount from a base currency to different currency using that currency's exchange rate
    public Double calcExchangeAmount(Double baseAmount, Double rate){ return baseAmount * rate; }

}
