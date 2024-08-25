package com.geoshare.backend.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.GeoshareUserDTO;
import com.geoshare.backend.dto.SetEmailDTO;
import com.geoshare.backend.dto.SetPasswordDTO;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.repository.GeoshareUserRepository;
import com.geoshare.backend.security.SecurityUser;

@Service
public class GeoshareUserService {

	private GeoshareUserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private UserCache userCache;
	
	public GeoshareUserService(GeoshareUserRepository userRepository, PasswordEncoder passwordEncoder, UserCache userCache) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userCache = userCache;
	}
	
	public void createUser(GeoshareUserDTO userDTO) {
		GeoshareUser userEntity = new GeoshareUser(
				userDTO.username(),
				passwordEncoder.encode(userDTO.password())
		);
		
		userRepository.save(userEntity);
	}
	
	public Long findUserIdByUsername(String username) {
		return userRepository.findByUsernameOrThrow(username).getId();
	}
	
	public GeoshareUser findUserByUsernameOrThrow(String username) {
		return userRepository.findByUsernameOrThrow(username);
	}
	
	public List<GeoshareUser> findAllUsers() {
		return userRepository.findAll();
	}
	
	//Maybe notify old email that email has been changed that would be nice feature
	public void setEmail(SetEmailDTO emailDTO, Authentication auth) {
		
		GeoshareUser user = userRepository.findByUsernameOrThrow(auth.getName());
		if (!passwordEncoder.matches(emailDTO.password(), user.getPassword())) {
			throw new AccessDeniedException("Password does not match stored value.");
		}
		
		
		//Validate emailDTO.newEmail()
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);
		if(!pattern.matcher(emailDTO.newEmail()).matches()) {
			throw new IllegalArgumentException("Provided e-mail is not a valid e-mail address.");
		}
		
		user.setEmail(emailDTO.newEmail());
		userRepository.save(user);
		
		//Refresh the user in the cache just in case of future changes to user validation
		userCache.removeUserFromCache(user.getUsername());
	}
	
	//Maybe have to check if password contains some weird characters or something?
	public void setPassword(SetPasswordDTO passwordDTO, Authentication auth) {
		GeoshareUser user = userRepository.findByUsernameOrThrow(auth.getName());
		if (!passwordEncoder.matches(passwordDTO.password(), user.getPassword())) {
			throw new AccessDeniedException("Password does not match stored value.");
		}
		
		user.setPassword(passwordEncoder.encode(passwordDTO.newPassword()));
		
		userRepository.save(user);
		
		//Refresh the user in the cache since the password was changed
		userCache.removeUserFromCache(user.getUsername());
		userCache.putUserInCache(new SecurityUser(user));
	}
	
}
