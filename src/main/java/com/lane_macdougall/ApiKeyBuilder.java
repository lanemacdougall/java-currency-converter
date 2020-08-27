package com.lane_macdougall;

/* PURPOSE: Builder object is used to configure an ApiKey object with the API key(s) provided by the implementation.
 *
 * Note: the implementation can use one or more of the supported APIs - the implementation can provide one or more key.
 */
public class ApiKeyBuilder {

    private final ApiKey apiKey;

    public ApiKeyBuilder() {
        this.apiKey = new ApiKey();
    }

    public ApiKeyBuilder setExchangeRateApiKey(String key) {
        this.apiKey.setExchangeRateAPIKey(key);
        return this;
    }

    public ApiKeyBuilder setCurrencyLayerApiKey(String key) {
        this.apiKey.setCurrencyLayerAPIKey(key);
        return this;
    }

    public ApiKey build() {
        if (this.apiKey.getExchangeRateAPIKey() == null && this.apiKey.getCurrencyLayerAPIKey() == null) {
            throw new CurrencyConverterException("No valid API key provided.");
        }

        return this.apiKey;
    }
}
