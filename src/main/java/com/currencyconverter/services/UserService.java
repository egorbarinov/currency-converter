package com.currencyconverter.services;

import com.currencyconverter.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);
    Iterable<User> getAllUser();
    User addAuditEntry(String principal, String queryString);
    User getAuditHistoryForUser(String username);
    User createNewQueryHistoryForUser(Principal principal);
    User saveQueryHistory(User queryHistoryForUser);

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    boolean saveUser(User user);

}
