# java-currency-converter

Real-time currency converter library for Java applications

Supports 2 data sources - Exchange Rate API (https://www.exchangerate-api.com/) and currencylayer (https://currencylayer.com/)

168 currencies (ISO 4217 three-letter currency codes)


# Documentation

## Download

Download Currency Converter.jar and add it to your project's dependencies.
See specific IDE documentation for details. Link for IntelliJ instructions: https://www.jetbrains.com/help/idea/working-with-module-dependencies.html

## Use 

### Initialization

Initialize a CurrencyConverter object using an APIKeyBuilder object and your API access key(s).

'''
/* Define at least one of the API key constants with your API access key(s).
 * The service will work with access to one API, however, for optimal service 
 * performance, it is recommended that you define both.
*/
private static final String EXCHANGE_RATE_API_KEY = null;
private static final String CURRENCY_LAYER_API_KEY = null;

// Example base and conversion currencies
private static final String BASE_CURRENCY = "USD";
private static final String EXCHANGE_CURRENCY = "JPY";
private static final String EXCHANGE_CURRENCY_TWO = "GBP";

CurrencyConverter converter = new CurrencyConverter(
    new ApiKeyBuilder()
            .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
            .setCurrencyLayerApiKey(CURRENCY_LAYER_API_KEY)
            .build()
);
'''
