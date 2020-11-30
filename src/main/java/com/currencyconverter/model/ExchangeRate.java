package com.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema="xml_valcurs", name = "course")
public class ExchangeRate {

    @Id
    @Column(columnDefinition = "Date")
    private LocalDate date;

    @Column(columnDefinition = "Valute")
    @ManyToMany(targetEntity = Valute.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @CollectionTable(schema = "xml_valcurs", name = "rate_valute_mapping", joinColumns = {@JoinColumn(name = "course_date", referencedColumnName = "date")})
    private Map<String, Valute> valute;


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, Valute> getValute() {
        return valute;
    }

    public void setValute(Map<String, Valute> valute) {
        this.valute = valute;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "date=" + date +
                ", valute=" + valute +
                '}';
    }
}
