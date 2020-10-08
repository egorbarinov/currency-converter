package com.currencyconverter.repositories;

import com.currencyconverter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
