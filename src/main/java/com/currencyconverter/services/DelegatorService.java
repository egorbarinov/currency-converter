package com.currencyconverter.services;

import com.currencyconverter.dto.CurrencyDto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Component
public class DelegatorService {

    private final ExchangeRateService exchangeRateService;
    private final UserServiceImpl userService;

    public DelegatorService(ExchangeRateService exchangeRateService, UserServiceImpl userService) {
        this.exchangeRateService = exchangeRateService;
        this.userService = userService;
    }

    /**
     * метод для выполнения конвертации данных, присваивающий dto значения конвертации и даты
     */
    public void performCurrencyConversion(CurrencyDto currencyDto, LocalDate date) {
        List<BigDecimal> valuesToDecimal = new ArrayList<>();

        addValuesToCurrency(date, valuesToDecimal, currencyDto.getCurrencyFrom());
        addValuesToCurrency(date, valuesToDecimal, currencyDto.getCurrencyTo());

        BigDecimal resultAmount = (valuesToDecimal.get(0))
                .divide(valuesToDecimal.get(1), 10, RoundingMode.CEILING)
                .multiply(currencyDto.getAmountToConvert());

        currencyDto.setConvertedAmount(resultAmount);
        currencyDto.setConversionDate(new Date());

    }

    private void addValuesToCurrency(LocalDate date, List<BigDecimal> valuesToDecimal, String currencyValue) {

        if (currencyValue.equals("RUB")) {
            valuesToDecimal.add(BigDecimal.valueOf(1));
        } else {
            CurrencyDto dto = exchangeRateService.getAll(date).stream().filter(v -> v.getCharCode().equals(currencyValue)).findFirst().orElseThrow();
            BigDecimal result = dto.getValue().divide(dto.getNominal());
            valuesToDecimal.add(result);
        }
    }

    /**
     * Метод добавляет выполненный запрос в историю запросов пользователя
     */
    public void performAudit(CurrencyDto currencyDto, Principal principal) {
        String auditString = currencyDto.getAuditString();
        userService.addAuditEntry(principal.getName(), auditString);

    }

}

