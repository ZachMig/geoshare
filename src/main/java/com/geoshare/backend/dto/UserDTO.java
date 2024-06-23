package com.geoshare.backend.dto;

public class UserDTO {
	
	private String username;
	private String password;
	private String tempPassword;
	
	public UserDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
}
