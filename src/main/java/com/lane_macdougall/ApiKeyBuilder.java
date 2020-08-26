package com.lane_macdougall;

/* PURPOSE: Builder object is used to configure an ApiKey object with the API key(s) provided by the implementation.
 *
 * Note: the implementation can use one or more of the supported APIs - i.e., the implementation can provide one or more
 * key.
 */
// NOTE: This class will be much more important when additional APIs are supported by the library
public class ApiKeyBuilder {

    private final ApiKey apiKey;

    public ApiKeyBuilder() {
        this.apiKey = new ApiKey();
    }

    public ApiKeyBuilder setExchangeRateApiKey(String key) {
        this.apiKey.setExchangeRateAPIKey(key);
        return this;
    }

    public ApiKey build() {
        if (this.apiKey.getExchangeRateAPIKey() == null) {
            throw new CurrencyConverterException("No API key provided.");
        }

        return this.apiKey;
    }
}
