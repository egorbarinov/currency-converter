package com.currencyconverter.services;

import com.currencyconverter.dao.ExchangeRateRepository;
import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.mapper.ValuteMapper;
import com.currencyconverter.model.ExchangeRate;
import com.currencyconverter.model.Valute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        return exchangeRateRepository.findExchangeRateByDate(date).getValute();

    }

    @Override
    public ExchangeRate findByDate(LocalDate date) {
        return exchangeRateRepository.findExchangeRateByDate(date);
    }

    public void processingUploadData(LocalDate date) throws IOException, ParserConfigurationException, SAXException {
//        LocalDate date = LocalDate.of(1993, 1,6); // Date when currency exchange rates started being saved;
        if (exchangeRateRepository.findExchangeRateByDate(date) == null) {

            logger.info("Ready records rates: " + LocalDateTime.now() + ".");

            while (date.compareTo(LocalDate.now()) < 0) {
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + LocalDate.now().format(formatters));
//                URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date
//                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int status = con.getResponseCode();
                if (status == HttpURLConnection.HTTP_OK) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = dbFactory.newDocumentBuilder();
                    Document document =builder.parse(String.valueOf(url));

                    NodeList valcurs = document.getDocumentElement().getElementsByTagName("Valute");
                    Map<String, Valute> valutesMap = new HashMap<>();
                    ExchangeRate rate = new ExchangeRate();
                    for (int i = 0; i < valcurs.getLength(); i++) {

                        Node node = valcurs.item(i);
                        String id = node.getAttributes().getNamedItem("ID").getNodeValue();
                        String numCode = document.getDocumentElement().getElementsByTagName("NumCode").item(i).getTextContent();
                        String charCode = document.getDocumentElement().getElementsByTagName("CharCode").item(i).getTextContent();
                        BigDecimal nominal = new BigDecimal(String.valueOf(document.getDocumentElement().getElementsByTagName("Nominal").item(i).getTextContent()));
                        String name = document.getDocumentElement().getElementsByTagName("Name").item(i).getTextContent();
                        BigDecimal value = new BigDecimal(document.getDocumentElement().getElementsByTagName("Value").item(i).getTextContent().replaceAll(",","\\."));
                        rate.setDate(date);
                        Valute valute = new Valute(id, numCode, charCode, nominal, name, value);

                        valutesMap.put(charCode, valute);
                        rate.setDate(date);
                        rate.setValute(valutesMap);
                    }
                    exchangeRateRepository.save(rate);
                } else if (status == HttpURLConnection.HTTP_NOT_FOUND) {
                    System.out.println("HTTP NOT FOUND");
                }
                date = date.plusDays(1);
            }
            logger.info("All records rates for today is saved! " + LocalDateTime.now() + ".");
        }

    }

    // http://www.cbr.ru/scripts/XML_daily.asp?date_req=30/11/2020
    @Override
    @Scheduled(cron = "0 0/30 7-15 * * MON-FRI")
    public void processingHttpRequest() throws IOException, ParserConfigurationException, SAXException {
        if (exchangeRateRepository.findExchangeRateByDate(LocalDate.now()) == null) {

            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + LocalDate.now()
//                    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + LocalDate.now().format(formatters));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document document =builder.parse(String.valueOf(url));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                NodeList valcurs = document.getDocumentElement().getElementsByTagName("Valute");
                Map<String, Valute> valutesMap = new HashMap<>();
                ExchangeRate rate = new ExchangeRate();
                for (int i = 0; i < valcurs.getLength(); i++) {

                    Node node = valcurs.item(i);
                    String id = node.getAttributes().getNamedItem("ID").getNodeValue();
                    String numCode = document.getDocumentElement().getElementsByTagName("NumCode").item(i).getTextContent();
                    String charCode = document.getDocumentElement().getElementsByTagName("CharCode").item(i).getTextContent();
                    BigDecimal nominal = new BigDecimal(String.valueOf(document.getDocumentElement().getElementsByTagName("Nominal").item(i).getTextContent()));
                    String name = document.getDocumentElement().getElementsByTagName("Name").item(i).getTextContent();
                    BigDecimal value = new BigDecimal(document.getDocumentElement().getElementsByTagName("Value").item(i).getTextContent().replaceAll(",","\\."));

                    Valute valute = new Valute(id, numCode, charCode, nominal, name, value);

                    valutesMap.put(charCode, valute);

                    rate.setDate(LocalDate.now());
                    rate.setValute(valutesMap);
                }
                exchangeRateRepository.save(rate);
                logger.info("All record saved!");
            }
        }
    }

}
