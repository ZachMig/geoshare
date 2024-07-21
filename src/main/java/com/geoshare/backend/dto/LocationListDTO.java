package com.geoshare.backend.dto;

import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationListDTO(
		@Max(value = Long.MAX_VALUE)
		Long id,
		
		@Size(max = 65535)
		@NotBlank
		String name,
		
		@Size(max = 65535)
		@NotBlank
		String description,
		
		boolean isPublic,
		
		Set<LocationDTO> locations)
{}
