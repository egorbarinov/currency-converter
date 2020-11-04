package com.currencyconverter.dao;


import com.currencyconverter.model.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEntryRepositoryDao extends JpaRepository<AuditEntry, Long> {

}
