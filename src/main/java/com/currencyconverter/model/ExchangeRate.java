package com.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema="valcurs", name = "course")
@JsonIgnoreProperties(value = {"name", "ID"})
public class ExchangeRate {

    @Id
    @JsonProperty("Date")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
////            pattern = "yyyy-MM-dd")
            pattern = "dd.MM.yyyy")
////            pattern = "yyyy-MM-dd'T'kk:mm:ssXXX")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @JsonProperty("Valute")
    @ManyToMany(targetEntity = Valute.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @CollectionTable(schema = "valcurs", name = "rate_valute_mapping", joinColumns = {@JoinColumn(name = "course_date", referencedColumnName = "date")})
    private Map<String, Valute> valute;


//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }
//
//    public Map<String, Valute> getValute() {
//        return valute;
//    }
//
//    public void setValute(Map<String, Valute> valute) {
//        this.valute = valute;
//    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "date=" + date +
                ", valute=" + valute +
                '}';
    }
}
