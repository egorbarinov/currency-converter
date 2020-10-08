package com.currencyconverter.services;

import com.currencyconverter.entities.ExchangeRate;
import com.currencyconverter.entities.Valute;
import com.currencyconverter.repositories.ExchangeRateRepositoryDao;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.ZoneId;
import java.util.*;

@Service
@EnableScheduling
public class ExchangeRateServiceImpl implements ExchangeRateService{
    private ExchangeRateRepositoryDao exchangeRateRepositoryDao;

    private final static Logger logger = LoggerFactory.getLogger(ExchangeRate.class);

    @Autowired
    public void setExchangeRateRepositoryDao(ExchangeRateRepositoryDao exchangeRateRepositoryDao) {
        this.exchangeRateRepositoryDao = exchangeRateRepositoryDao;
    }

    @Override
    public Iterable<ExchangeRate> getAllExchangeRate() {
        return exchangeRateRepositoryDao.findAll();
    }

    @Override
    public Map <String, Valute> getAllValute() {
        Calendar date = returnDate();
        return exchangeRateRepositoryDao.findById(date).get().getValute();
    }

    @Override
    public ExchangeRate findById() {
        Calendar date = returnDate();
        return exchangeRateRepositoryDao.findById(date).get();
    }

    /**
     * Метод приводит текущую дату к виду: Wed Oct 07 00:00:00 MSK 2020, в дальнейшем
     * его значение используется для поиска курсов валют в базе данных.
     * В базе данных значение хранится в формате: 2020-10-08 00:00:00
     */
    private Calendar returnDate() {
        LocalDate ld = LocalDate.now(ZoneId.of("Europe/Moscow"));
        Date dateNow = java.sql.Date.valueOf(ld); // приводит дату к виду 2020-10-08
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        date.setTime(dateNow);
        return date;
    }

    /**
     * метод парсит данные из http-запроса и сохраняет их в базу данных
     * посредством библиотеки Jackson
     */
    @Override
    public int processingHttpRequest() throws IOException {
        URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {

            ObjectMapper objectMapper = new ObjectMapper();

            ExchangeRate rate = objectMapper.readValue(url, ExchangeRate.class);
            exchangeRateRepositoryDao.save(rate);
            logger.info("All records saved.");
        }
        return status;
    }

    /**
     * Защита от недоступности сведений из URL-адреса: метод либо выполнит сохранение данных,
     * либо вернёт false;
     */
    @Override
    @Scheduled(cron = "0 0/30 7-15 * * MON-FRI")
    public boolean isLoaded() throws IOException {
        int status = processingHttpRequest();
        if (status == 200) {
           return true;
        }
        return false;
    }

}
