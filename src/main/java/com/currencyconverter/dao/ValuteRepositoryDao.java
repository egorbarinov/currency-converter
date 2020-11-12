package com.currencyconverter.dao;

import com.currencyconverter.model.Valute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValuteRepositoryDao extends JpaRepository<Valute, Long> {
}
