package com.currencyconverter.services;

import com.currencyconverter.dto.UserDto;
import com.currencyconverter.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);
    boolean existByName(String username);
    boolean existByEmail(String username);
    User findByUserEmail(String email);
    List<UserDto> getAll();
    User addAuditEntry(String principal, String queryString);
    User getAuditHistoryForUser(String username);
    User createNewQueryHistoryForUser(Principal principal);
    User saveQueryHistory(User queryHistoryForUser);
    void saveUser(UserDto userDto);
    boolean activateUser(String activateCode);

//    Map<String, String> validate(UserDto userForm);

}
