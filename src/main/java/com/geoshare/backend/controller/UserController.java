package com.geoshare.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@GetMapping()
	public String home() {
		return "Hello World";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "Hello admin";
	}
	
}
