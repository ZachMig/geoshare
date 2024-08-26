package com.geoshare.backend.dto;

import java.util.Collection;

import jakarta.annotation.Nullable;
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
	
	@Size(max = 65535)
	@NotNull
	String countryName,
	
	@Size(max = 65535)
	@NotBlank
	String meta,
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	Long userID,
	
	@Nullable
	@Size(max = 65535)
	Collection<Long> listIDs
	)
	{}