package com.currencyconverter.services;

import com.currencyconverter.dao.ExchangeRateRepository;
import com.currencyconverter.dto.CurrencyDto;
import com.currencyconverter.dto.ExchangeRateDto;
import com.currencyconverter.mapper.CurrencyMapper;
import com.currencyconverter.mapper.ExchangeRateMapper;
import com.currencyconverter.model.ExchangeRate;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ExchangeRateServiceImpl implements ExchangeRateService{

    private CurrencyMapper currencyMapper;
    private ExchangeRateMapper rateMapper;

    @Autowired
    public void setCurrencyMapper(CurrencyMapper currencyMapper) {
        this.currencyMapper = currencyMapper;
    }

    @Autowired
    public void setRateMapper(ExchangeRateMapper rateMapper) {
        this.rateMapper = rateMapper;
    }

    private ExchangeRateRepository exchangeRateRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

    @Autowired
    public void setExchangeRateRepository(ExchangeRateRepository exchangeRateRateRepository) {
        this.exchangeRateRepository = exchangeRateRateRepository;

    }

    @Override
    public List<CurrencyDto> getAll() {
        LocalDate date =LocalDate.now();
        return currencyMapper.fromCurrencyList(exchangeRateRepository.findExchangeRateByDate(date).getCurrencies());
    }

    @Override
    public List<CurrencyDto> getAll(LocalDate date) {
        List<CurrencyDto> lists = currencyMapper.fromCurrencyList(findExchangeRateByDate(date).getCurrencies());
        lists.sort(Comparator.comparing(CurrencyDto::getName));
        return lists;
    }

    @Override
    public ExchangeRate findExchangeRateByDate(LocalDate date) {
        return exchangeRateRepository.findExchangeRateByDate(date);
    }

    @Override
    public void loadCbrfRates(LocalDate date) throws IOException {
        if (exchangeRateRepository.findExchangeRateByDate(date) == null && LocalDate.now().compareTo(date) >=0) {
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date.format(formatters));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                saveToExchangeRate(url, date);
            }
        }
    }

    private void saveToExchangeRate(URL url, LocalDate date) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ExchangeRateDto rateDto = xmlMapper.readValue(url, ExchangeRateDto.class);
        rateDto.setDate(date);
        ExchangeRate rate = rateMapper.toExchangeRate(rateDto);

//        ModelExchangeRate modelRate = xmlMapper.readValue(url, ExchangeRate.class);
//        converter.convert(modelRate);
//        rate.setDate(date);
        exchangeRateRepository.save(rate);
        logger.info("All records rates for today is saved! ");
    }

    @Override
    public void processingUploadData(LocalDate date) throws IOException {
        if (exchangeRateRepository.findExchangeRateByDate(date) == null) {
            logger.info("Ready records rates {} :", LocalDateTime.now());

            while (date.compareTo(LocalDate.now()) <= 0) {
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date.format(formatters));

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int status = con.getResponseCode();
                if (status == HttpURLConnection.HTTP_OK) {
                    saveToExchangeRate(url, date);
                } else if (status == HttpURLConnection.HTTP_NOT_FOUND) {
                    logger.info("HTTP NOT FOUND");
                }
                date = date.plusDays(1);
            }
            logger.info("All records rates for today is saved {} ! ", LocalDateTime.now());
        }
    }


}
