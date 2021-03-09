package com.currencyconverter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

import static com.currencyconverter.common.CurrencyConverterUtil.getFormattedAmount;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    private Long pk;
    private String id;
    private String numCode;
    private String charCode;
    private BigDecimal nominal;
    private String name;
    private BigDecimal value;
    private String currencyFrom;
    private String currencyTo;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 11, fraction = 2)
    private BigDecimal amountToConvert;

    private BigDecimal convertedAmount;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date conversionDate;

    public void setValue(String value) {
        this.value = new BigDecimal(value.replaceAll(",", "\\."));
    }


    @Override
    public String toString() {
        return "ValuteDto{" +
                "pk=" + pk +
                ", id='" + id + '\'' +
                ", numCode='" + numCode + '\'' +
                ", charCode='" + charCode + '\'' +
                ", nominal=" + nominal +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", currencyFrom='" + currencyFrom + '\'' +
                ", currencyTo='" + currencyTo + '\'' +
                ", amountToConvert=" + amountToConvert +
                ", convertedAmount=" + convertedAmount +
                ", conversionDate=" + conversionDate +
                '}';
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
