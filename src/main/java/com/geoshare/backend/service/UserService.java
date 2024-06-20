package com.geoshare.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.User;
import com.geoshare.backend.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	//@Autowired
	//private PasswordEncoder passwordEncoder;
	
	public User registerUser(User user) {
        //TODO?
        return userRepository.save(user);
    }
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
