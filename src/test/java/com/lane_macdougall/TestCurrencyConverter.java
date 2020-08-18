package com.lane_macdougall;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TestCurrencyConverter {

    private static final String EXCHANGE_RATE_API_KEY = "YOUR API KEY";
    private static final String BASE_CURRENCY = "USD";
    private static final String EXCHANGE_CURRENCY = "JPY";

    @Test
    public void retrieveRatesTest(){

        CurrencyConverter converter = new CurrencyConverter(
                new ApiKeyBuilder()
                        .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
                        .build(),
                BASE_CURRENCY
        );
        converter.rates();
        assertThat(converter.getAllExchangeRates()).isNotNull();
    }

    @Test
    public void retrieveExchangeRateTest(){
        CurrencyConverter converter = new CurrencyConverter(
                new ApiKeyBuilder()
                        .setExchangeRateApiKey(EXCHANGE_RATE_API_KEY)
                        .build(),
                BASE_CURRENCY
        );
        converter.rates();
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
        converter.rates();
        Double rate = converter.getExchangeRate(EXCHANGE_CURRENCY);

        Double baseAmount = 10d;
        Double amount = converter.calcExchangeAmount(baseAmount, rate);

        assertThat(amount).isGreaterThan(baseAmount);
    }


}
