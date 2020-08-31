package com.lane_macdougall;

import com.lane_macdougall.api_keys.ApiKeyBuilder;
import com.lane_macdougall.utility.ApiRequestUtility;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestCurrencyConverter {
    // TODO: BE SURE TO SET EXCHANGE_RATE_API_KEY TO YOUR API ACCESS KEY(s)
    private static final String EXCHANGE_RATE_API_KEY = null;
    private static final String CURRENCY_LAYER_API_KEY = null;
    private static final String BASE_CURRENCY = "USD";
    private static final String EXCHANGE_CURRENCY = "JPY";
    private static final String EXCHANGE_CURRENCY_TWO = "GBP";

    /* PURPOSE: Test that the single request mechanism is working properly; i.e., if the exchange rates have already
     * been requested and successfully retrieved, simply retrieve the desired rate from the existing exchange rate
     * hash map.
     */
    @Test
    public void singleRequestTest() throws IOException {
        CurrencyConverter converter = new CurrencyConverter(
                new ApiKeyBuilder()
                        .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
                        .setCurrencyLayerApiKey(CURRENCY_LAYER_API_KEY)
                        .build()
        );
        ApiRequestUtility.setRequestCount(0);
        converter.getExchangeRate(BASE_CURRENCY, EXCHANGE_CURRENCY);
        converter.getExchangeRate(BASE_CURRENCY, EXCHANGE_CURRENCY_TWO);
        assertThat(ApiRequestUtility.getRequestCount()).isEqualTo(1);

    }

    /* PURPOSE: Test that the CurrencyConverter class' getExchangeRate() method (retrieves a specific exchange rate)
     * is working as expected.
     *
     * Here, the U.S. Dollar to Japanese Yen exchange rate is retrieved and the retrieved rate is (under normal conditions)
     * expected to be significantly greater than 0 (hence, the assertion of isGreaterThan(0)).
     */
    @Test
    public void retrieveExchangeRateTest() throws IOException {
        CurrencyConverter converter = new CurrencyConverter(
                new ApiKeyBuilder()
                        .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
                        .setCurrencyLayerApiKey(CURRENCY_LAYER_API_KEY)
                        .build()
        );
        Double rate = converter.getExchangeRate(BASE_CURRENCY, EXCHANGE_CURRENCY);
        assertThat(rate).isGreaterThan(0);
    }

    /* PURPOSE: Test that the CurrencyConverter class' convertAmount() method (converts an amount in the specified base
     * currency to the corresponding amount in the specified exchange currency) is working as expected.
     *
     * Here, 10 U.S. dollars are converted to Japanese Yen. The exchange rate is (under normal conditions) expected to
     * be significantly greater than 0 and, thus, the converted amount is expected to be greater than the base amount
     * (hence, the assertion of isGreaterThan(baseAmount)).
     */
    @Test
    public void convertAmountTest() throws IOException {
        CurrencyConverter converter = new CurrencyConverter(
                new ApiKeyBuilder()
                        .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
                        .setCurrencyLayerApiKey(CURRENCY_LAYER_API_KEY)
                        .build()
        );

        double baseAmount = 10;
        Double amount = converter.convertAmount(baseAmount, BASE_CURRENCY, EXCHANGE_CURRENCY);
        assertThat(amount).isGreaterThan(baseAmount);
    }


}
