package com.currencyconverter.services;

import com.currencyconverter.dto.CurrencyDto;
import com.currencyconverter.model.ExchangeRate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateService {

    void loadCbrfRates() throws IOException;

    void processingUploadData(LocalDate localDate) throws IOException;
    ExchangeRate findExchangeRateByDate(LocalDate date);
    List<CurrencyDto> getAll();
    List<CurrencyDto> getAll(LocalDate date);
}
