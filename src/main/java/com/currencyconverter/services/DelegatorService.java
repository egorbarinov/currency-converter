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
    public void performCurrencyConversion(ValuteDto valuteDto, LocalDate date) {
//        LocalDate date = LocalDate.now();
        String[] resultValues = valuteDto.toString().split(",");
        List<BigDecimal> valuesToDecimal = new ArrayList<>();
        for (int i = 0; i < resultValues.length; i++) {
            if (resultValues[i].equals("RUB")) {
                BigDecimal result = BigDecimal.valueOf(1);
                valuesToDecimal.add(result);
            } else {
//                BigDecimal result = exchangeRateService.findById().getValute()
//                        .get(resultValues[i]).getValue()
//                        .divide(exchangeRateService.findById()
//                                .getValute().get(resultValues[i]).getNominal());
//                valuesToDecimal.add(result);

                BigDecimal result = exchangeRateService.getAllValute(date)
                        .get(resultValues[i]).getValue()
                        .divide(exchangeRateService.getAllValute(date)
                                .get(resultValues[i]).getNominal());
                valuesToDecimal.add(result);
            }
        }

        BigDecimal resultAmount = (valuesToDecimal.get(0))
                .divide(valuesToDecimal.get(1), 10, RoundingMode.HALF_UP)
                .multiply(valuteDto.getAmountToConvert());

        valuteDto.setConvertedAmount(resultAmount);
        valuteDto.setConversionDate(new Date());

    }

    /**
     * Метод добавляет выполненный запрос в историю запросов пользователя
     */
    public void performAudit(ValuteDto valuteDto, Principal principal) {
        String auditString = valuteDto.getAuditString();
        userService.addAuditEntry(principal.getName(), auditString);

    }

}
