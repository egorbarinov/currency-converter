package com.currencyconverter.dto;

import com.currencyconverter.model.Valute;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@JacksonXmlRootElement(localName = "ValCurs")
@JsonIgnoreProperties(value = {"name"})
public class ExchangeRateDto {
    @Id
    @JacksonXmlProperty(localName = "Date")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd.MM.yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Valute" )
    private List<ValuteDto> valutes;

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "date=" + date +
                ", valute=" + valutes +
                '}';
    }
}
