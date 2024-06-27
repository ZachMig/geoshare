package com.geoshare.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LocationCommentDTO {
	
	@Size(max = 65535)
	@NotNull
	private String content;
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	private Long locationID;
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	private Long userID;

	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getLocationID() {
		return locationID;
	}

	public void setLocationID(Long locationID) {
		this.locationID = locationID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

}
