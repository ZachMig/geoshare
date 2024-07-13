package com.geoshare.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LocationCommentDTO(
	
	@Size(max = 65535)
	@NotNull
	String content,
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	Long locationID,
	
	@Max(value = Long.MAX_VALUE)
	Long parentCommentID
	) 
{}

