package com.currencyconverter.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    private Long pk;
    @JacksonXmlProperty(localName = "ID", isAttribute = true)
    private String id;
    @JacksonXmlProperty(localName = "NumCode")
    private String numCode;
    @JacksonXmlProperty(localName = "CharCode")
    private String charCode;
    @JacksonXmlProperty(localName = "Nominal")
    private BigDecimal nominal;
    @JacksonXmlProperty(localName = "Name")
    private String name;
    @JacksonXmlProperty(localName = "Value")
    private BigDecimal value;
    private String currencyFrom;
    private String currencyTo;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 11, fraction = 2)
    private BigDecimal amountToConvert;

    private BigDecimal convertedAmount;

    private Date conversionDate;

    public void setValue(String value) {
        this.value = new BigDecimal(value.replace(",", "\\.")); // value.replaceAll
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

    private String getFormattedAmount(BigDecimal amountToFormat) {
        return Optional.ofNullable(amountToFormat).map(p -> {
            p = p.setScale(4, RoundingMode.CEILING);
            return String.format("%1$17s", new DecimalFormat("0.0000").format(p));
        }).orElse(null);
    }

    public boolean isSelected(String valuteDtoCharCode){
        if (valuteDtoCharCode != null) {
            return valuteDtoCharCode.equals(charCode);
        }
        return false;
    }
}
