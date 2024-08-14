package com.geoshare.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.repository.GeoshareUserRepository;

@EnableCaching
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	
//	@Bean
//	CommandLineRunner commandLinerUnner(GeoshareUserRepository users, PasswordEncoder encoder) {
//		return args -> {
//			users.save(new GeoshareUser("user", encoder.encode("password"), "ROLE_USER"));
//		};
//	}
	
}
