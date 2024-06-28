package com.geoshare.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GeoshareUserDTO (
	
	@Size(max = 255)
	@NotBlank
	String username,
	
	@Size(max = 255)
	@NotBlank
	String password
	)
	{}

//	@Max(value = 255)
//	@Email
//	@NotBlank
//	private String email;


