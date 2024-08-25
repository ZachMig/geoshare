package com.geoshare.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetPasswordDTO (
		@Size(max = 255)
		@NotBlank
		String password,
		@Size(max = 255)
		@NotBlank
		String newPassword)
{}
