package com.geoshare.backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.GeoshareUserDTO;
import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.repository.GeoshareUserRepository;

@Service
public class GeoshareUserService {

	private GeoshareUserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public GeoshareUserService(GeoshareUserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
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
	
	public List<GeoshareUser> findAllUsers() {
		return userRepository.findAll();
	}
	
}
