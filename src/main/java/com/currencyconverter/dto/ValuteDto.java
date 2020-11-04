package com.currencyconverter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

import static com.currencyconverter.common.CurrencyConverterUtil.getFormattedAmount;

@Data
@NoArgsConstructor
public class ValuteDto {
    private Long pk;
    private String charCode;
    private BigDecimal nominal;
    private String name;
    private BigDecimal value;
    private BigDecimal previous;

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

    public boolean isSelected(String ValuteDtoCharCode){
        if (ValuteDtoCharCode != null) {
            return ValuteDtoCharCode.equals(charCode);
        }
        return false;
    }
}
