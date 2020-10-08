package com.currencyconverter.viewModel;

import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

import static com.currencyconverter.common.CurrencyConverterUtil.*;

public class ViewCurrencies {

    @NotBlank
    private String currencyFrom;

    @NotBlank
    private String currencyTo;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 11, fraction = 2)
    private BigDecimal amountToConvert;

    private BigDecimal convertedAmount;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date conversionDate;


    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public BigDecimal getAmountToConvert() {
        return amountToConvert;
    }

    public void setAmountToConvert(BigDecimal amountToConvert) {
        this.amountToConvert = amountToConvert;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public Date getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    @Override
    public String toString() {
        return String.format("%s,%s", currencyFrom, currencyTo);
    }

    public String getAuditString() {
        StringBuilder auditString = new StringBuilder("Запрос создан: ")
                .append(getConversionDate())
                .append(";&emsp;")
                .append(getFormattedAmount(getAmountToConvert()))
                .append(" ")
                .append(getCurrencyFrom())
                .append(" конвертация в ")
                .append(getCurrencyTo())
                .append(" . Получен результат: ")
                .append(getFormattedAmount(getConvertedAmount()))
                .append(" ")
                .append(getCurrencyTo());
        return auditString.toString();
    }

}
