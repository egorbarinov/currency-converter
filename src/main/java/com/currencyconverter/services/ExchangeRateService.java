package com.currencyconverter.services;

import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.model.ExchangeRate;
import com.currencyconverter.model.Valute;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateService {

    void processingHttpRequest() throws IOException;
    void processingUploadData(LocalDate localDate) throws IOException;
    ExchangeRate findExchangeRateByDate(LocalDate date);
    List<ValuteDto> getAll();
    List<ValuteDto> getAll(LocalDate date);
}
