package com.currencyconverter.services;

import com.currencyconverter.dto.ExchangeRateDto;
import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.mapper.ExchangeRateMapper;
import com.currencyconverter.model.ExchangeRate;
import com.currencyconverter.mapper.ValuteMapper;
import com.currencyconverter.dao.ExchangeRateRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@EnableScheduling
public class ExchangeRateServiceImpl implements ExchangeRateService{

    private final ValuteMapper mapper = ValuteMapper.MAPPER;
    private final ExchangeRateMapper rateMapper = ExchangeRateMapper.MAPPER;

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
        List<ValuteDto> lists = mapper.fromValuteList(exchangeRateRepository.findExchangeRateByDate(date).getValutes());
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
        List<ValuteDto> lists = mapper.fromValuteList(exchangeRateRepository.findExchangeRateByDate(date).getValutes());
        lists.sort(Comparator.comparing(ValuteDto::getName));
        return lists;
    }

    @Override
    public Iterable<ExchangeRate> getAllExchangeRate() {
        return exchangeRateRepository.findAll();
    }


    @Override
    public ExchangeRate findByDate(LocalDate date) {
        return exchangeRateRepository.findExchangeRateByDate(date);
    }

    @Override
    public ExchangeRate findByCharCode(String charCode) {
        return exchangeRateRepository.findExchangeRateByCharCode(charCode);
    }

    /**
     * метод парсит данные из http-запроса и сохраняет их в базу данных
     * посредством библиотеки Jackson
     */
    @Override
    @Scheduled(cron = "0 0/30 7-15 * * MON-FRI")
    public void processingHttpRequest() throws IOException, ParserConfigurationException, SAXException {
        if (exchangeRateRepository.findExchangeRateByDate(LocalDate.now()) == null) {
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + LocalDate.now().format(formatters));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {

                XmlMapper xmlMapper = new XmlMapper();

                ExchangeRateDto rateDto = xmlMapper.readValue(url, ExchangeRateDto.class);
                ExchangeRate rate = rateMapper.toExchangeRate(rateDto);
                exchangeRateRepository.save(rate);
                logger.info("All record saved!");
            }
        }

    }

}
