package com.lane_macdougall;

import java.text.DecimalFormat;
import java.util.Map;


/* PURPOSE: Retrieve the supported exchange rates and then retrieve specific rates or convert money amounts
 *
 */
public class CurrencyConverter {

    private final ApiKey key;

    // ISO 4217 three-letter code of base currency
    private String baseCurr;

    private Map<String, Double> allExchangeRates;


    /* Constructor */
    public CurrencyConverter(ApiKey key, String baseCurr) {
        this.key = key;
        this.baseCurr = baseCurr.toUpperCase();
    }

    /* Methods */

    public String getBaseCurr() {
        return baseCurr;
    }

    // Provide base currency's ISO 4217 three-letter code
    public void setBaseCurr(String baseCurr) {
        this.baseCurr = baseCurr;
    }

    // Retrieve a specific exchange rate (using currency's ISO 4217 three-letter code)
    public Double getExchangeRate(String to) {
        /* If the exchange rates have been retrieved successfully and the specified currency is supported,
         * return its exchange rate
         */
        return ServiceCoordinator.retrieveRates(this.key, this.baseCurr, to.toUpperCase());
    }

    // Convert an amount from a base currency to different currency using that currency's exchange rate
    public Double convertAmount(Double baseAmount, String to) {
        Double convertedAmount = baseAmount * getExchangeRate(to);
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        return Double.parseDouble(decimalFormat.format(convertedAmount));
    }

}
