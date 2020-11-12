package com.currencyconverter.services;

import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.model.ExchangeRate;
import com.currencyconverter.model.Valute;
import com.currencyconverter.mapper.ValuteMapper;
import com.currencyconverter.dao.ExchangeRateRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@EnableScheduling
public class ExchangeRateServiceImpl implements ExchangeRateService{

    private final ValuteMapper mapper = ValuteMapper.MAPPER;

    private ExchangeRateRepository exchangeRateRepository;

    private final static Logger logger = LoggerFactory.getLogger(ExchangeRate.class);

    public ExchangeRateServiceImpl() throws IOException {
    }

    @Autowired
    public void setExchangeRateRepository(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;

    }

    // перевёл сущность в DTO
    @Override
    public List<ValuteDto> getAll() {
        LocalDate date =LocalDate.now();
        List<ValuteDto> lists = mapper.fromValuteList(exchangeRateRepository.findExchangeRateByDate(date).getValute().values());
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
        List<ValuteDto> lists = mapper.fromValuteList(exchangeRateRepository.findExchangeRateByDate(date).getValute().values());
        lists.sort(Comparator.comparing(ValuteDto::getName));
        return lists;
    }

    @Override
    public Iterable<ExchangeRate> getAllExchangeRate() {
        return exchangeRateRepository.findAll();
    }

    @Override
    public Map <String, Valute> getAllValute(LocalDate date) {
//        return exchangeRateRepositoryDao.findById(date).get().getValute();
        return exchangeRateRepository.findExchangeRateByDate(date).getValute();

    }

//    @Override
//    public ExchangeRate findById() {
//        LocalDate date =LocalDate.now();
//        return exchangeRateRepositoryDao.findById(date).get();
//    }

    @Override
    public ExchangeRate findByDate(LocalDate date) {
        return exchangeRateRepository.findExchangeRateByDate(date);
    }

    /**
     * метод парсит данные из http-запроса и сохраняет их в базу данных
     * посредством библиотеки Jackson
     */
    @Override
    @Scheduled(cron = "0 0/30 7-15 * * MON-FRI")
    public void processingHttpRequest() throws IOException {
        LocalDate firstDate = LocalDate.of(2020,10,1);
        if (exchangeRateRepository.findExchangeRateByDate(LocalDate.now()) == null) {
            processingUploadData(firstDate);
        }

        if (exchangeRateRepository.findExchangeRateByDate(LocalDate.now()) == null ||
                exchangeRateRepository.findExchangeRateByDate(LocalDate.now().plusDays(1)) == null) {

            URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                ExchangeRate rate = objectMapper.readValue(url, ExchangeRate.class);
                if (LocalDate.now().compareTo(rate.getDate()) > 0 &&
                        exchangeRateRepository.findExchangeRateByDate(LocalDate.now()) == null) {
                    logger.info("The current date is greater than the date of rate from the json-file. Updating the date in the json file data:  " + LocalDateTime.now() + ".");
                    rate.setDate(LocalDate.now());
                    exchangeRateRepository.save(rate);
                } else if (LocalDate.now().compareTo(rate.getDate()) == 0 &&
                        exchangeRateRepository.findExchangeRateByDate(LocalDate.now()) == null) {
                    exchangeRateRepository.save(rate);
                }
                else if (LocalDate.now().compareTo(rate.getDate()) < 0 &&
                        exchangeRateRepository.findExchangeRateByDate(LocalDate.now().plusDays(1)) == null) {
                    exchangeRateRepository.save(rate);
                }
                else return;
            }
            logger.info("All records saved " + LocalDateTime.now() + ".");
        }

    }

    private void processingUploadData(LocalDate date) throws IOException {
//        LocalDate date = LocalDate.of(1993, 1,6); // Date when currency exchange rates started being saved;
        if (exchangeRateRepository.findExchangeRateByDate(date) == null) {
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            ObjectMapper objectMapper = new ObjectMapper();
            logger.info("Ready records rates: " + LocalDateTime.now() + ".");

            while (date.compareTo(LocalDate.now()) < 0) {
                URL url = new URL("https://www.cbr-xml-daily.ru/archive/" + date.format(formatters) + "/daily_json.js");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int status = con.getResponseCode();
                if (status == HttpURLConnection.HTTP_OK) {
                    ExchangeRate rate = objectMapper.readValue(url, ExchangeRate.class);
                    exchangeRateRepository.save(rate);
                    logger.info("All records rates for today is saved! " + LocalDateTime.now() + ".");
                } else if (status == HttpURLConnection.HTTP_NOT_FOUND) {
                    ExchangeRate rate  = exchangeRateRepository.findExchangeRateByDate(date.minusDays(1));
                    rate.setDate(date);
                    exchangeRateRepository.save(rate);
                }
                date = date.plusDays(1);

            }
            logger.info("All records rates for today is saved! " + LocalDateTime.now() + ".");
        }

    }

}
