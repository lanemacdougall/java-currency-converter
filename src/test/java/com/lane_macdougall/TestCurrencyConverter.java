package com.lane_macdougall;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TestCurrencyConverter {
    // TODO: BE SURE TO SET EXCHANGE_RATE_API_KEY TO YOUR API ACCESS KEY
    private static final String EXCHANGE_RATE_API_KEY = "f92a462856fc8ef3d4db524e";
    private static final String BASE_CURRENCY = "USD";
    private static final String EXCHANGE_CURRENCY = "JPY";

    @Test
    public void retrieveExchangeRateTest(){
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
    public void calculateExchangeAmountTest(){
        CurrencyConverter converter = new CurrencyConverter(
                new ApiKeyBuilder()
                        .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
                        .build(),
                BASE_CURRENCY
        );
        Double rate = converter.getExchangeRate(EXCHANGE_CURRENCY);

        Double baseAmount = 10d;
        Double amount = converter.calcExchangeAmount(baseAmount, rate);

        assertThat(amount).isGreaterThan(baseAmount);
    }


}
