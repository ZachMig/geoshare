package com.geoshare.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geoshare.backend.dto.GeoshareUserDTO;
import com.geoshare.backend.service.GeoshareUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private GeoshareUserService userService;
	
	public UserController(GeoshareUserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/create")
	public HttpStatus createUser(
			@Valid
			@RequestBody GeoshareUserDTO userDTO) {
		
		try {
			this.userService.createUser(userDTO);
			return HttpStatus.OK;
		} catch (Exception e) {
			return HttpStatus.BAD_REQUEST;
		}
	}
	
//	@PostMapping("/create")
//	public ResponseEntity<?> createUser(
//			@Valid
//			@RequestBody GeoshareUserDTO userDTO) {
//		
//		try {
//			this.userService.createUser(userDTO);
//			return new ResponseEntity<>(HttpStatus.OK);
//		} catch (Exception e) {
//	        return new ResponseEntity(this.errorMapper.createErrorMap(e), HttpStatus.BAD_REQUEST);
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//		
//	}
	
}
