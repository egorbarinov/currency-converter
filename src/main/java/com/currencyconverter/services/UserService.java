package com.currencyconverter.services;

import com.currencyconverter.dto.UserDto;
import com.currencyconverter.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);
    User findByUserEmail(String email);
    List<UserDto> getAll();
//    Iterable<User> getAllUser();
    User addAuditEntry(String principal, String queryString);
    User getAuditHistoryForUser(String username);
    User createNewQueryHistoryForUser(Principal principal);
    User saveQueryHistory(User queryHistoryForUser);

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

//    boolean saveUser(UserDto userDto);
    void saveUser(UserDto userDto);

}
