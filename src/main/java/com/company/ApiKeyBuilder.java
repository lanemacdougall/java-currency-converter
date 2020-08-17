package com.company;

/* PURPOSE: Builder object is used to configure an ApiKey object with the API key(s) provided by the implementation.
 *
 * Note: the implementation can use one or more of the supported APIs - i.e., the implementation can provide one or more
 * key.
 */
public class ApiKeyBuilder {

    private ApiKey apiKey;

    public ApiKeyBuilder(){ this.apiKey = new ApiKey(); }

    public void setExchangeRateApiKey(String key){ this.apiKey.setExchangeRateAPIKey(key); }

    public ApiKey build(){
        if (this.apiKey.getExchangeRateAPIKey() == null) {
            throw new CurrencyConverterException("No API key provided.");
        }

        return this.apiKey;
    }
}
