package com.geoshare.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LocationDTO (
	
	@Size(max = 4096)
	@NotBlank
	String url,
	
	@Size(max = 65535)
	@NotBlank
	String description,
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	Long countryID,
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	Long userID
	)
	{}
	
	
//	@Size(max = 32)
//	@NotBlank
//	private String country;

