package com.currencyconverter.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Map;

@Entity
@Table(schema="json", name = "course")
public class ExchangeRate {

    @Id
    @JsonProperty("Date")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd")
//            pattern = "yyyy-MM-dd'T'hh:mm:ssXXX")
    private Calendar date;

    @JsonProperty("PreviousDate")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'hh:mm:ssXXX")
    private Calendar previousDate;

    @JsonProperty("PreviousURL")
    private String previousURL;

    @JsonProperty("Timestamp")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'hh:mm:ssXXX")
    private Calendar timestamp;

    @ManyToMany(targetEntity = Valute.class, cascade = CascadeType.ALL)
    @CollectionTable(schema = "json", name = "rate_valute_mapping", joinColumns = {@JoinColumn(name = "course_date", referencedColumnName = "date")})
    private Map<String, Valute> valute;

    public ExchangeRate() {

    }

    public ExchangeRate(Calendar date, Calendar previousDate, String previousURL, Calendar timestamp, Map<String, Valute> valute) {
        this.date = date;
        this.previousDate = previousDate;
        this.previousURL = previousURL;
        this.timestamp = timestamp;
        this.valute = valute;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Calendar getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(Calendar previousDate) {
        this.previousDate = previousDate;
    }

    public String getPreviousURL() {
        return previousURL;
    }

    public void setPreviousURL(String previousURL) {
        this.previousURL = previousURL;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("Valute")
    public Map<String, Valute> getValute() {
        return valute;
    }

    @JsonProperty("Valute")
    public void setValute(Map<String, Valute> valute) {
        this.valute = valute;
    }
}
