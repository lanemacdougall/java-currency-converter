package com.company;

/* PURPOSE: Object is used to contain the keys of the supported APIs.
 *
 * Note: the implementation can use one or more of the supported APIs - i.e., the implementation can provide one or more
 * key.
 */
public class ApiKey {

    private String exchangeRateAPIKey;

    public void setExchangeRateAPIKey(String exchangeRateAPIKey){ this.exchangeRateAPIKey = exchangeRateAPIKey; }

    public String getExchangeRateAPIKey(){ return this.exchangeRateAPIKey; }

}
