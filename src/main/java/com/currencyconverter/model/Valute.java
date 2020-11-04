package com.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "valcurs", name="currencies")
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @JsonProperty("ID")
    private String id;
    @JsonProperty("NumCode")
    private String numCode;
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

//    public boolean isSelected(String ValuteCharCode){
//        if (ValuteCharCode != null) {
//            return ValuteCharCode.equals(charCode);
//        }
//        return false;
//    }

}
