package com.geoshare.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="user")
@Data
public class GeoshareUser {
	
	public GeoshareUser() {}
	
	public GeoshareUser(String username, String password) {
		this.username = username;
		this.password = password;
		this.roles = "ROLE_USER";
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	
	@Column(name="username", unique=true)
	private String username;
	
	@JsonIgnore
	@Column(name="password")
	private String password;
	
	@JsonIgnore
	@Column(name="roles")
	private String roles;

	@JsonIgnore
	@Column(name="email")
	private String email;
	
}
