package com.lane_macdougall;

import com.lane_macdougall.api_keys.ApiKey;
import com.lane_macdougall.service_coordinator.ServiceCoordinator;

import java.io.IOException;
import java.text.DecimalFormat;


/* PURPOSE: Retrieve the supported exchange rates and then retrieve specific rates or convert money amounts.
 *
 */
public class CurrencyConverter {

    private final ApiKey key;

    /* Constructor */
    public CurrencyConverter(ApiKey key) {
        this.key = key;
    }

    /* Methods */


    /* PURPOSE: Retrieve a specific exchange rate (using (base and conversion) currencies' ISO 4217 three-letter code).
     *
     * RETURNS: Exchange rate between specified base currency and conversion currency (from and to params).
     */
    public Double getExchangeRate(String from, String to) throws IOException {
        /* If the exchange rates have been retrieved successfully and the specified currency is supported,
         * return its exchange rate
         */
        return ServiceCoordinator.retrieveRates(this.key, from, to.toUpperCase());
    }


    /* PURPOSE: Convert an amount from a base currency to different currency (specified with ISO 4217 three-letter
     * codes) using those currencies' exchange rate.
     *
     * RETURNS: Amount in base currency converted to amount in conversion currency.
     */
    public Double convertAmount(double baseAmount, String from, String to) throws IOException {
        Double convertedAmount = baseAmount * getExchangeRate(from, to);

        // Format currency to two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        return Double.parseDouble(decimalFormat.format(convertedAmount));
    }

}
