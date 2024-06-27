package com.geoshare.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LocationDTO {
	
	@Size(max = 4096)
	@NotBlank
	private String url;
	
	@Size(max = 65535)
	@NotBlank
	private String description;
	
	@Size(max = 32)
	@NotBlank
	private String country;
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	private Long userID;

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}
}
