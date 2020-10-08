package com.currencyconverter.services;

import com.currencyconverter.entities.Role;
import com.currencyconverter.entities.User;
import com.currencyconverter.repositories.UserRepositoryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepositoryDao userRepositoryDAO;

	@Autowired
	public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Autowired
	public void setUserRepositoryDAO(UserRepositoryDao userRepositoryDAO) {
		this.userRepositoryDAO = userRepositoryDAO;
	}

	@Override
	public User findByUsername(String username) {
	return userRepositoryDAO.findByUsername(username);
	}


	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException(String.format("User '%s' not found", username));
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}


	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
	}

	@Override
	public Iterable<User> getAllUser() {
		return userRepositoryDAO.findAll();
	}

	private static final Logger logger = LoggerFactory.getLogger(User.class);

	@Override
	public User addAuditEntry(String username, String queryString) {
		logger.debug("Adding Audit for user " + username + " ::" + queryString);
		User queryHistoryForUser = userRepositoryDAO.findByUsername(username);
		queryHistoryForUser.addNewAuditEntry(queryString);
		saveQueryHistory(queryHistoryForUser);

		return queryHistoryForUser;
	}

	@Override
	public User getAuditHistoryForUser(String username) {
		User queryHistory = userRepositoryDAO.findByUsername(username);
		return queryHistory;
	}

	@Override
	public User createNewQueryHistoryForUser(Principal principal) {
		return new User(principal.getName());
	}

	@Override
	public User saveQueryHistory(User queryHistoryForUser) {
		return userRepositoryDAO.save(queryHistoryForUser);
	}

	public boolean saveUser(User user) {
		User userFromDB = userRepositoryDAO.findByUsername(user.getUsername());

		if (userFromDB != null) {
			return false;
		}

		user.setRoles(Collections.singleton(new Role("ROLE_USER")));
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEmail(user.getEmail());
		user.setEnabled(true);
		userRepositoryDAO.save(user);
		return true;
	}

}
