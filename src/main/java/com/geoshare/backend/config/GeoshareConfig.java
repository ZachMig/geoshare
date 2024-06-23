package com.geoshare.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SuppressWarnings("deprecation")
@Configuration
public class GeoshareConfig {

	@Bean
	public UserDetailsService uds() {
		InMemoryUserDetailsManager im = new InMemoryUserDetailsManager();
		
		UserDetails userDetails = User.withUsername("user")
				.password("password")
				.authorities("USER_ROLE")
				.build();
		
		im.createUser(userDetails);
		
		return im;
	
	}
	  
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();	
		//return new BCryptPasswordEncoder();
	}
	
}
