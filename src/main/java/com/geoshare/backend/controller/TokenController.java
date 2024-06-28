package com.geoshare.backend.controller;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.dto.LoginRequest;
import com.geoshare.backend.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class TokenController {

	private final TokenService tokenService;
	private final AuthenticationManager authManager;
	private final PasswordEncoder passwordEncoder;
	
	public TokenController(TokenService tokenService, AuthenticationManager authManager, PasswordEncoder passwordEncoder) {
		this.tokenService = tokenService;
		this.authManager = authManager;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	@PostMapping("/gettoken")
	public String issueToken(@RequestBody LoginRequest loginRequest) throws AuthenticationException {
		log.info("Username: " + loginRequest.username() + " and password: " + loginRequest.password());
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.username(), 
						loginRequest.password()));
		String token = tokenService.generateToken(authentication);
		log.info(token);
		return token;
	}
	
	
}
