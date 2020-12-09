package com.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema="jxmlparse_valcurs", name = "course")

@JacksonXmlRootElement(localName = "ValCurs")
@JsonIgnoreProperties(value = {"name"})
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

//    @JsonProperty("Valute")
//    @ManyToMany(targetEntity = Valute.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @CollectionTable(schema = "valcurs", name = "rate_valute_mapping", joinColumns = {@JoinColumn(name = "course_date", referencedColumnName = "date")})
//    private Map<String, Valute> valute;

    @JacksonXmlElementWrapper(useWrapping = false)
//    @JacksonXmlProperty(namespace = "Valute" )
    @JsonProperty("Valute")
//    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
//    @OneToMany(targetEntity = Valute.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
////    OneToMany

    @ManyToMany(cascade=CascadeType.ALL)
//    @JoinTable(
//            schema = "jxmlparse_valcurs",
//            name = "rate_valute_mapping",
//            joinColumns = @JoinColumn(name = "course_date"),
//            inverseJoinColumns = @JoinColumn(name = "valute_key"))
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
