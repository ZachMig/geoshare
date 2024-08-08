package com.geoshare.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.dto.GeoshareUserDTO;
import com.geoshare.backend.service.GeoshareUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class GeoshareUserController {

	private GeoshareUserService userService;
	
	public GeoshareUserController(GeoshareUserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<String> createUser(
			@Valid
			@RequestBody GeoshareUserDTO userDTO) {
		
		userService.createUser(userDTO);
		
		return new ResponseEntity<>("User create request handled successfully.", HttpStatus.OK);
	}
	
	@GetMapping("/findall")
	public ResponseEntity<?> findAllUsers() {
		return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
	}
	
}
