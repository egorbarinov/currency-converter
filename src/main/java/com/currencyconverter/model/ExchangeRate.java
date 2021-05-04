package com.currencyconverter.model;

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
@NamedQuery(name = "withValutes", query = "SELECT e FROM ExchangeRate e JOIN FETCH e.currencies WHERE e.date = :date") // for LAZY upload
public class ExchangeRate {

    @Id
    private LocalDate date;

    @OneToMany(targetEntity = Currency.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionTable(schema = "jxmlparse_valcurs",
            name = "rate_valute_mapping",
            joinColumns = {@JoinColumn(name = "course_date",
                    referencedColumnName = "date")})
//    @JoinTable(schema = "jxmlparse_valcurs",
//            name = "rate_valute_mapping",
//    joinColumns = {@JoinColumn(name = "course_date",
//                    referencedColumnName = "date")},
//    inverseJoinColumns = @JoinColumn(name = "valute_pk"))
    private List<Currency> currencies;

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "date=" + date +
                ", valute=" + currencies +
                '}';
    }
}
