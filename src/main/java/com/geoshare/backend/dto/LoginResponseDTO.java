package com.geoshare.backend.dto;

public record LoginResponseDTO(
		String jwt,
		Long userID)

{}
