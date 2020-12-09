package com.currencyconverter.services;

import com.currencyconverter.dto.ValuteDto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
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
    public void performCurrencyConversion(ValuteDto valuteDto) {
        List<BigDecimal> valuesToDecimal = new ArrayList<>();

        addValuesToCurrency(valuesToDecimal, valuteDto.getCurrencyFrom());
        addValuesToCurrency(valuesToDecimal, valuteDto.getCurrencyTo());

        BigDecimal resultAmount = (valuesToDecimal.get(0))
                .divide(valuesToDecimal.get(1), 10, RoundingMode.HALF_UP)
                .multiply(valuteDto.getAmountToConvert());

        valuteDto.setConvertedAmount(resultAmount);
        valuteDto.setConversionDate(new Date());

    }

    private void addValuesToCurrency(List<BigDecimal> valuesToDecimal, String currencyValue) {

        if (currencyValue.equals("RUB")) {
            valuesToDecimal.add(BigDecimal.valueOf(1));
        } else {
            BigDecimal result = exchangeRateService.findByCharCode(currencyValue)
                    .getValue()
                    .divide(exchangeRateService.findByCharCode(currencyValue).getNominal());

            valuesToDecimal.add(result);
        }
    }

    /**
     * Метод добавляет выполненный запрос в историю запросов пользователя
     */
    public void performAudit(ValuteDto valuteDto, Principal principal) {
        String auditString = valuteDto.getAuditString();
        userService.addAuditEntry(principal.getName(), auditString);

    }

}

