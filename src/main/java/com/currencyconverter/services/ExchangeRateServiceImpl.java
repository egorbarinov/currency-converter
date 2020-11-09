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
import java.time.LocalDateTime;
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
        List<ValuteDto> lists = mapper.fromValuteList(exchangeRateRepositoryDao.findExchangeRateByDate(date).getValute().values());
//        lists.sort(new Comparator<ValuteDto>() {
//            @Override
//            public int compare(ValuteDto o1, ValuteDto o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
//        lists.sort((v1, v2) -> v1.getCharCode().compareTo(v2.getCharCode()));
        lists.sort(Comparator.comparing(ValuteDto::getName));
        return lists;
    }

    @Override
    public List<ValuteDto> getAll(LocalDate date) {
//        LocalDate localDate = LocalDate.parse(date);
        List<ValuteDto> lists = mapper.fromValuteList(exchangeRateRepositoryDao.findExchangeRateByDate(date).getValute().values());
        lists.sort(Comparator.comparing(ValuteDto::getName));
        return lists;
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
        if (exchangeRateRepositoryDao.findExchangeRateByDate(LocalDate.now()) == null ||
                exchangeRateRepositoryDao.findExchangeRateByDate(LocalDate.now().plusDays(1)) == null) {

            URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                ExchangeRate rate = objectMapper.readValue(url, ExchangeRate.class);
                if (LocalDate.now().compareTo(rate.getDate()) > 0 &&
                        exchangeRateRepositoryDao.findExchangeRateByDate(LocalDate.now()) == null) {
                    logger.info("The current date is greater than the date of rate from the json-file. Updating the date in the json file data:  " + LocalDateTime.now() + ".");
                    rate.setDate(LocalDate.now());
                    exchangeRateRepositoryDao.save(rate);
                } else if (LocalDate.now().compareTo(rate.getDate()) == 0 &&
                        exchangeRateRepositoryDao.findExchangeRateByDate(LocalDate.now()) == null) {
                    exchangeRateRepositoryDao.save(rate);
                }
                else if (LocalDate.now().compareTo(rate.getDate()) < 0 &&
                        exchangeRateRepositoryDao.findExchangeRateByDate(LocalDate.now().plusDays(1)) == null) {
                    exchangeRateRepositoryDao.save(rate);
                }
                else return;
            }
            logger.info("All records saved " + LocalDateTime.now() + ".");
        }

    }

}
