package com.currencyconverter.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

import static com.currencyconverter.common.CurrencyConverterUtil.getFormattedAmount;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JacksonXmlRootElement(localName = "Valute")
public class ValuteDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JacksonXmlProperty(namespace = "pk")
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

    public void setValue(String value) {
        this.value = new BigDecimal(value.replaceAll(",", "\\."));
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

    public boolean isSelected(String ValuteDtoCharCode){
        if (ValuteDtoCharCode != null) {
            return ValuteDtoCharCode.equals(charCode);
        }
        return false;
    }
}
