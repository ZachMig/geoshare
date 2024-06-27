package com.geoshare.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ListCommentDTO {
	
	@Size(max = 65535)
	@NotNull
	private String content;
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	private Long listID;
	
	@Max(value = Long.MAX_VALUE)
	@NotNull
	private Long userID;

	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getListID() {
		return listID;
	}

	public void setListID(Long listID) {
		this.listID = listID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	
}
