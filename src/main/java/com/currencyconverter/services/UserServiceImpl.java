package com.currencyconverter.services;

import com.currencyconverter.dto.UserDto;
import com.currencyconverter.model.Role;
import com.currencyconverter.model.User;
import com.currencyconverter.dao.UserRepositoryDao;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
	public User findByUserEmail(String email) {
		return userRepositoryDAO.findByEmail(email);
	}

	@Override
	public boolean existByName(String username) {
		return findByUsername(username) != null;
	}

	@Override
	public boolean existByEmail(String email) {
		return findByUserEmail(email) != null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("User not found with name: " + username);
		}

		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority(user.getRole().name()));

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				roles);
	}

//	@Override
//	@Transactional
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = findByUsername(username);
//		if(user == null) {
//			throw new UsernameNotFoundException(String.format("User '%s' not found", username));
//		}
//		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//				mapRolesToAuthorities(user.getRoles()));
//	}
//
//
//	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role2> role2s) {
//		return role2s.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
//	}

//	@Override
//	public Iterable<User> getAllUser() {
//		return userRepositoryDAO.findAll();
//	}

	@Override
	public List<UserDto> getAll() {
		return userRepositoryDAO.findAll().stream()
				.map(this::toDto)
				.collect(Collectors.toList());
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

//	public boolean saveUser(UserDto userDto) {
//		User userFromDB = userRepositoryDAO.findByUsername(userDto.getUsername());
//
//		boolean created = true;
//		if (userFromDB != null) {
//			created = false;
//		}
//
//		userFromDB = userRepositoryDAO.findByEmail(userDto.getEmail());
//		if (userFromDB != null) {
//			created = false;
//		} else created = true;
//
//		if (created) {
//			User user = User.builder()
//					.username(userDto.getUsername())
//					.password(bCryptPasswordEncoder.encode(userDto.getPassword()))
//					.email(userDto.getEmail())
//					.role(Role.CLIENT)
//					.enabled(false)
//					.build();
//			userRepositoryDAO.save(user);
//			return true;
//		}
//		return false;
//
//	}

	public void saveUser(UserDto userDto) {
		User user = User.builder()
				.username(userDto.getUsername())
				.password(bCryptPasswordEncoder.encode(userDto.getPassword()))
				.email(userDto.getEmail())
				.role(Role.CLIENT)
				.enabled(false)
				.build();
		userRepositoryDAO.save(user);
	}

	private UserDto toDto(User user){
		return UserDto.builder()
				.username(user.getUsername())
				.email(user.getEmail())
				.build();
	}

}
