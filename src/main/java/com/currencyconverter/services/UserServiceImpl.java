package com.currencyconverter.services;

import com.currencyconverter.dto.UserDto;
import com.currencyconverter.model.AuditEntry;
import com.currencyconverter.model.Role;
import com.currencyconverter.model.User;
import com.currencyconverter.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validation;
import javax.validation.Validator;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
	private final MailSenderService mailSenderService;

	public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, MailSenderService mailSenderService) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
		this.mailSenderService = mailSenderService;
	}

	@Override
	public User findByUsername(String username) {
	return userRepository.findByUsername(username);
	}

	@Override
	public User findByUserEmail(String email) {
		return userRepository.findByEmail(email);
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
		return userRepository.findAll().stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	private static final Logger logger = LoggerFactory.getLogger(User.class);

	@Override
	public User addAuditEntry(String username, String queryString) {
		logger.debug("Adding Audit for user " + username + " ::" + queryString);
		User queryHistoryForUser = userRepository.findByUsername(username);
		addNewAuditEntry(queryHistoryForUser, queryString);
		saveQueryHistory(queryHistoryForUser);

		return queryHistoryForUser;
	}

	public void addNewAuditEntry(User user, String queryString) {
		AuditEntry newEntry = createNewAuditEntry(queryString);
		if (user.auditEntries.size() == 100)
			user.auditEntries.remove(99);
		user.auditEntries.add(0,newEntry);
	}

	public AuditEntry createNewAuditEntry(String queryString) {
		return new AuditEntry(queryString, new Date());
	}

	@Override
	public User getAuditHistoryForUser(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User createNewQueryHistoryForUser(Principal principal) {
		return new User(principal.getName());
	}

	@Override
	public User saveQueryHistory(User queryHistoryForUser) {
		return userRepository.save(queryHistoryForUser);
	}

	@Override
	@Transactional
	public void saveUser(UserDto userDto) {
		User user = User.builder()
				.username(userDto.getUsername())
				.password(bCryptPasswordEncoder.encode(userDto.getPassword()))
				.email(userDto.getEmail())
				.role(Role.CLIENT)
				.activateCode(UUID.randomUUID().toString())
				.enabled(false)
				.build();
		userRepository.save(user);
		if(user.getActivateCode() != null && !user.getActivateCode().isEmpty()){
			new Thread(() -> mailSenderService.sendActivateCode(user)).start();
		}
	}

	private UserDto toDto(User user){
		return UserDto.builder()
				.username(user.getUsername())
				.email(user.getEmail())
				.activated(user.getActivateCode() == null)
				.build();
	}

	@Override
	@Transactional
	public boolean activateUser(String activateCode) {
		if(activateCode == null || activateCode.isEmpty()){
			return false;
		}
		User user = userRepository.findByActivateCode(activateCode);
		if(user == null){
			return false;
		}
		user.setActivateCode(null);
		user.setEnabled(true);
		userRepository.save(user);

		return true;
	}

//	public Map<String, String> validate(UserDto userForm) {
//		Map<String, String> messages = new HashMap<>();
//		if (userForm.getUsername() == null || userForm.getPassword() == null || userForm.getPasswordConfirm() == null || userForm.getEmail() == null) {
//			messages.put("errorForm", "Все поля должны быть заполнены");
//		}
//
//		boolean existing = existByName(userForm.getUsername());
//		if (existing) {
//			userForm.setUsername(null);
//			messages.put("registrationError", "Пользователь с таким логином уже существует");
//		}
//
//		existing = existByEmail(userForm.getEmail());
//		if (existing) {
//			userForm.setEmail(null);
//			messages.put("mailError", "Пользователь с таким email уже существует");
//		}
//
//		if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
//			userForm.setPassword(null);
//			userForm.setPasswordConfirm(null);
//			messages.put("passwordError", "Пароли не совпадают!");
//		}
//		return messages;
//	}


}
