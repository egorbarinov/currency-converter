package com.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "xml_valcurs", name="currencies")
public class Valute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(columnDefinition = "ID")
    private String id;
    @Column(columnDefinition = "NumCode")
    private String numCode;
    @Column(columnDefinition = "CharCode")
    private String charCode;
    @Column(columnDefinition = "Nominal")
    private BigDecimal nominal;
    @Column(columnDefinition = "Name")
    private String name;
    @Column(columnDefinition = "Value")
    private BigDecimal value;

    public Valute(String id, String numCode, String charCode, BigDecimal nominal, String name, BigDecimal value) {
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }
}
