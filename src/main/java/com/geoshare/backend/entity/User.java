package com.geoshare.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Long id;
	
	@Column(name="email", unique=true)
	private String email;
	
	@Column(name="username", unique=true)
	private String username;
	
	@Column(name="hashed_pw")
	private String hashedPW;
	
	@Column(name="hashed_temp_pw")
	private String hashedTempPW;
	
	@Column(name="salt")
	private byte[] salt;
	
}
