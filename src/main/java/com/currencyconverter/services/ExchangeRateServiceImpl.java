package com.currencyconverter.services;

import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.model.ExchangeRate;
import com.currencyconverter.model.Valute;
import com.currencyconverter.mapper.ValuteMapper;
import com.currencyconverter.dao.ExchangeRateRepositoryDao;
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
import java.util.*;

@Service
@EnableScheduling
public class ExchangeRateServiceImpl implements ExchangeRateService{

    private final ValuteMapper mapper = ValuteMapper.MAPPER;

    private ExchangeRateRepositoryDao exchangeRateRepositoryDao;

    private final static Logger logger = LoggerFactory.getLogger(ExchangeRate.class);

    @Autowired
    public void setExchangeRateRepositoryDao(ExchangeRateRepositoryDao exchangeRateRepositoryDao) {
        this.exchangeRateRepositoryDao = exchangeRateRepositoryDao;

    }

    // перевёл сущность в DTO
    @Override
    public List<ValuteDto> getAll() {
        LocalDate date =LocalDate.now();
//        return mapper.fromValuteList(exchangeRateRepositoryDao.findById(date).get().getValute().values());
        return mapper.fromValuteList(exchangeRateRepositoryDao.findExchangeRateByDate(date).getValute().values());
    }

    @Override
    public Iterable<ExchangeRate> getAllExchangeRate() {
        return exchangeRateRepositoryDao.findAll();
    }

    @Override
    public Map <String, Valute> getAllValute(LocalDate date) {

//        return exchangeRateRepositoryDao.findById(date).get().getValute();
        return exchangeRateRepositoryDao.findExchangeRateByDate(date).getValute();

    }

//    @Override
//    public ExchangeRate findById() {
//        LocalDate date =LocalDate.now();
//        return exchangeRateRepositoryDao.findById(date).get();
//    }

    @Override
    public ExchangeRate findByDate(LocalDate date) {
        return exchangeRateRepositoryDao.findExchangeRateByDate(date);
    }

    /**
     * метод парсит данные из http-запроса и сохраняет их в базу данных
     * посредством библиотеки Jackson
     */
    @Override
    @Scheduled(cron = "0 0/30 7-15 * * MON-FRI")
    public void processingHttpRequest() throws IOException {
       // "Date": "2020-11-04T11:30:00+03:00",

//        LocalDateTime date =LocalDate.now().atTime(0,0,0).atZone(ZoneId.of("+03:00")).toLocalDateTime();
//        System.out.println(date); // 2020-11-03T11:30
        LocalDate date = LocalDate.now();
//        LocalDate date = LocalDate.now().atTime(0,0,0).toLocalDate();
//        System.out.println(date); // 2020-11-03

        if (exchangeRateRepositoryDao.findExchangeRateByDate(date) == null) {

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
        }

    }

}
