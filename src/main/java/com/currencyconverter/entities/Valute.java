package com.currencyconverter.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(schema = "json", name="currencies")
@JsonPropertyOrder({
        "ID",
        "NumCode",
        "CharCode",
        "Nominal",
        "Name",
        "Value",
        "Previous"
})
public class Valute {


    @JsonProperty("ID")
    private String id;
    @JsonProperty("NumCode")
    private String numCode;
    @Id
    @JsonProperty("CharCode")
    private String charCode;
    @JsonProperty("Nominal")
    private BigDecimal nominal;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Value")
    private BigDecimal value;
    @JsonProperty("Previous")
    private BigDecimal previous;

    public Valute() {
    }

    public Valute(String id, String numCode, String charCode, BigDecimal nominal, String name, BigDecimal value, BigDecimal previous) {
        super();
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.previous = previous;
    }

    @JsonProperty("ID")
    public String getId() {
        return id;
    }

    @JsonProperty("ID")
    public void setID(String id) {
        this.id = id;
    }

    @JsonProperty("NumCode")
    public String getNumCode() {
        return numCode;
    }

    @JsonProperty("NumCode")
    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    @JsonProperty("CharCode")
    public String getCharCode() {
        return charCode;
    }

    @JsonProperty("CharCode")
    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    @JsonProperty("Nominal")
    public BigDecimal getNominal() {
        return nominal;
    }

    @JsonProperty("Nominal")
    public void setNominal(BigDecimal nominal) {
        this.nominal = nominal;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Value")
    public BigDecimal getValue() {
        return value;
    }

    @JsonProperty("Value")
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @JsonProperty("Previous")
    public BigDecimal getPrevious() {
        return previous;
    }

    @JsonProperty("Previous")
    public void setPrevious(BigDecimal previous) {
        this.previous = previous;
    }


    public boolean isSelected(String ValuteCharCode){
        if (ValuteCharCode != null) {
            return ValuteCharCode.equals(charCode);
        }
        return false;
    }

}
