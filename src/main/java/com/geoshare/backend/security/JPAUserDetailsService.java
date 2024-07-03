package com.geoshare.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.geoshare.backend.entity.GeoshareUser;
import com.geoshare.backend.repository.GeoshareUserRepository;

@Service
public class JPAUserDetailsService implements UserDetailsService {
	
	private GeoshareUserRepository userRepository;

	public JPAUserDetailsService(GeoshareUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		GeoshareUser geoUser = userRepository.findByUsernameOrThrow(username);
		
		return new SecurityUser(geoUser);
	}
	
}
