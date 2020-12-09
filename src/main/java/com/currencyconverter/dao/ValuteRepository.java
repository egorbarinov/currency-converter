package com.currencyconverter.dao;

import com.currencyconverter.model.Valute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValuteRepository extends JpaRepository<Valute, Long> {
    Valute findByCharCode(String charCode);
}
