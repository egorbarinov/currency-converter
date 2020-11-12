package com.currencyconverter.services;

import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.model.ExchangeRate;
import com.currencyconverter.model.Valute;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExchangeRateService {
    Iterable<ExchangeRate> getAllExchangeRate();
    Map<String, Valute> getAllValute(LocalDate date);
    void processingHttpRequest() throws IOException;
//    ExchangeRate findById();
    ExchangeRate findByDate(LocalDate date);
    List<ValuteDto> getAll();
    List<ValuteDto> getAll(LocalDate date);
}
