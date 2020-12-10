package com.currencyconverter.model;

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
@Entity
@Table(schema="jxmlparse_valcurs", name = "course")
@JacksonXmlRootElement(localName = "ValCurs")
@JsonIgnoreProperties(value = {"name"})
public class ExchangeRate {

    @Id
    @JacksonXmlProperty(localName = "Date")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
////            pattern = "yyyy-MM-dd")
            pattern = "dd.MM.yyyy")
////            pattern = "yyyy-MM-dd'T'kk:mm:ssXXX")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Valute" )
    @ManyToMany(targetEntity = Valute.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @CollectionTable(schema = "jxmlparse_valcurs",
            name = "rate_valute_mapping",
            joinColumns = {@JoinColumn(name = "course_date",
                    referencedColumnName = "date")})
    private List<Valute> valutes;

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "date=" + date +
                ", valute=" + valutes +
                '}';
    }
}
