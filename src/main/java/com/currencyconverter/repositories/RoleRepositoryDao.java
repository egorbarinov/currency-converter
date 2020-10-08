package com.currencyconverter.repositories;

import com.currencyconverter.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryDao extends JpaRepository<Role, Long> {
}
