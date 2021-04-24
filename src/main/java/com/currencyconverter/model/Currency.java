package com.currencyconverter.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "jxmlparse_valcurs", name="currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;
    @Column
    private String id;
    @Column
    private String numCode;
    @Column
    private String charCode;
    @Column
    private BigDecimal nominal;
    @Column
    private String name;
    @Column
    private BigDecimal value;

    public void setValue(String value) {
        this.value = new BigDecimal(value.replace(",", "."));
    }

}

