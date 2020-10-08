package com.currencyconverter.repositories;


import com.currencyconverter.entities.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEntryRepositoryDao extends JpaRepository<AuditEntry, Long> {

}
