package com.currencyconverter.services;

import com.currencyconverter.viewModel.ViewCurrencies;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class DelegatorService {

    private ExchangeRateService exchangeRateService;
    private UserServiceImpl userService;


    public DelegatorService(ExchangeRateService exchangeRateService, UserServiceImpl userService) {
        this.exchangeRateService = exchangeRateService;
        this.userService = userService;
    }

    /**
     * метод для выполнения конвертации данных, присваивающий сущности результат и дату выполнения операции
     */
    public void performCurrencyConversion(ViewCurrencies selectedCurrencies) {

        String[] resultValues = selectedCurrencies.toString().split(",");
        List<BigDecimal> valuesToDecimal = new ArrayList<>();
        for (int i = 0; i < resultValues.length; i++) {
            if (resultValues[i].equals("RUB")) {
                BigDecimal result = BigDecimal.valueOf(1);
                valuesToDecimal.add(result);
            } else {

                BigDecimal result = exchangeRateService.findById().getValute()
                        .get(resultValues[i]).getPrevious()
                        .divide(exchangeRateService.findById()
                                .getValute().get(resultValues[i]).getNominal());
                valuesToDecimal.add(result);
            }
        }

        BigDecimal resultAmount = (valuesToDecimal.get(0))
                .divide(valuesToDecimal.get(1), 10, RoundingMode.HALF_UP)
                .multiply(selectedCurrencies.getAmountToConvert());

        selectedCurrencies.setConvertedAmount(resultAmount);
        selectedCurrencies.setConversionDate(new Date());

    }

    /**
     * Метод добавляет выполненный запрос в историю запросов пользователя
     */
    public void performAudit(ViewCurrencies selectedCurrencies, String username) {
        String auditString = selectedCurrencies.getAuditString();
        userService.addAuditEntry(userService.findByUsername(username).getUsername(), auditString);

    }

}
