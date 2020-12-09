package com.currencyconverter.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "jxmlparse_valcurs", name="currencies")
@JacksonXmlRootElement(localName = "Valute")
public class Valute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JacksonXmlProperty
    @JsonProperty("valute_key")
    private Long pk;

    @JsonProperty("ID")
    @JacksonXmlProperty(isAttribute = true)
//    @JacksonXmlProperty(namespace = "ID")
    private String id;
    @JsonProperty("NumCode")
    private String numCode;
    @JsonProperty("CharCode")
    private String charCode;
    @JsonProperty("Nominal")
    private BigDecimal nominal;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Value")
    private String value;

}

