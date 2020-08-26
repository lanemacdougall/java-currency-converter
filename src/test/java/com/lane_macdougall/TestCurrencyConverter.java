package com.lane_macdougall;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestCurrencyConverter {
    // TODO: BE SURE TO SET EXCHANGE_RATE_API_KEY TO YOUR API ACCESS KEY
    private static final String EXCHANGE_RATE_API_KEY = "YOUR API ACCESS KEY";
    private static final String BASE_CURRENCY = "USD";
    private static final String EXCHANGE_CURRENCY = "JPY";

    @Test
    public void retrieveExchangeRateTest() {
        CurrencyConverter converter = new CurrencyConverter(
                new ApiKeyBuilder()
                        .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
                        .build(),
                BASE_CURRENCY
        );
        Double rate = converter.getExchangeRate(EXCHANGE_CURRENCY);
        assertThat(rate).isGreaterThan(0);
    }

    @Test
    public void convertAmountTest() {
        CurrencyConverter converter = new CurrencyConverter(
                new ApiKeyBuilder()
                        .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
                        .build(),
                BASE_CURRENCY
        );

        double baseAmount = 10;
        Double amount = converter.convertAmount(baseAmount, EXCHANGE_CURRENCY);
        assertThat(amount).isGreaterThan(baseAmount);
    }


}
