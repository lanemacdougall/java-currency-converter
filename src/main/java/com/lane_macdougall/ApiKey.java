package com.lane_macdougall;

/* PURPOSE: Object is used to contain the keys of the supported APIs.
 *
 * Note: the implementation can use one or more of the supported APIs - the implementation can provide one or more key.
 */
public class ApiKey {

    private String exchangeRateAPIKey = null;

    private String currencyLayerAPIKey = null;

    public String getExchangeRateAPIKey() {
        return this.exchangeRateAPIKey;
    }

    public void setExchangeRateAPIKey(String exchangeRateAPIKey) { this.exchangeRateAPIKey = exchangeRateAPIKey; }

    public String getCurrencyLayerAPIKey(){ return this.currencyLayerAPIKey; }

    public void setCurrencyLayerAPIKey(String currencyLayerAPIKey){ this.currencyLayerAPIKey = currencyLayerAPIKey; }

}
