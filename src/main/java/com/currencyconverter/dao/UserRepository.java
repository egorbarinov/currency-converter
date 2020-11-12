package com.currencyconverter.dao;

import com.currencyconverter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String username);
    User findByActivateCode(String activateCode);
}
