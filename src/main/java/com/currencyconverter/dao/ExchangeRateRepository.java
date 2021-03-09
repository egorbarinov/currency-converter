package com.currencyconverter.dao;

import com.currencyconverter.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, LocalDate> {
    ExchangeRate findExchangeRateByDate(LocalDate date);


}
