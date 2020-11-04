package com.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema="valcurs", name = "course")
public class ExchangeRate {

    @Id
    @JsonProperty("Date")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
//            pattern = "yyyy-MM-dd")
            pattern = "yyyy-MM-dd'T'kk:mm:ssXXX")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @JsonProperty("PreviousDate")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'kk:mm:ssXXX")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime previousDate;

    @JsonProperty("PreviousURL")
    private String previousURL;

    @JsonProperty("Timestamp")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'kk:mm:ssXXX")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;

    @JsonProperty("Valute")
    @ManyToMany(targetEntity = Valute.class, cascade = CascadeType.ALL)
    @CollectionTable(schema = "valcurs", name = "rate_valute_mapping", joinColumns = {@JoinColumn(name = "course_date", referencedColumnName = "date")})
    private Map<String, Valute> valute;


}
