package com.geoshare.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LocationDTO (
	
	@Max(value = Long.MAX_VALUE)
	Long id,
		
	@Size(max = 4096)
	@NotBlank
	String url,
	
	@Size(max = 65535)
	@NotBlank
	String description,
	
	//Change to country name
	@Max(value = Long.MAX_VALUE)
	@NotNull
	String countryName,
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	Long userID,
	
	@Size(max = 65535)
	@NotBlank
	String meta
	)
	{}
	
	
//	@Size(max = 32)
//	@NotBlank
//	private String country;

