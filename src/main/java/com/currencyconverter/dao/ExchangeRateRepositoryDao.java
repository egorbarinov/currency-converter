package com.currencyconverter.dao;

import com.currencyconverter.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExchangeRateRepositoryDao extends JpaRepository<ExchangeRate, LocalDate> {
    ExchangeRate findExchangeRateByDate(LocalDate date);
//    ExchangeRate findExchangeRatesByDate(LocalDate date);

}
