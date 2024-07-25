package com.geoshare.backend.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.dto.LoginRequestDTO;
import com.geoshare.backend.dto.LoginResponseDTO;
import com.geoshare.backend.service.GeoshareUserService;
import com.geoshare.backend.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class TokenController {

	private final TokenService tokenService;

	
	public TokenController(TokenService tokenService) {
		this.tokenService = tokenService;
	
	}
	
	//TODO
	//Move this to service layer
	@PostMapping("/gettoken")
	public ResponseEntity<LoginResponseDTO> issueToken(@RequestBody LoginRequestDTO loginRequest) {
		LoginResponseDTO loginResponse = tokenService.loginUser(loginRequest);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}
	
	
}
