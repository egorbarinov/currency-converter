package com.currencyconverter.repositories;

import com.currencyconverter.entities.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Calendar;

public interface ExchangeRateRepositoryDao extends JpaRepository<ExchangeRate, Calendar> {

}
