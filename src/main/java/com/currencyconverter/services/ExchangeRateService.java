package com.currencyconverter.services;

import com.currencyconverter.entities.ExchangeRate;
import com.currencyconverter.entities.Valute;
import java.io.IOException;
import java.util.Map;

public interface ExchangeRateService {
    Iterable<ExchangeRate> getAllExchangeRate();
    Map<String, Valute> getAllValute();
    int processingHttpRequest() throws IOException;
    boolean isLoaded() throws IOException;
    ExchangeRate findById();

}
