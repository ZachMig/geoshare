package com.geoshare.backend.service;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import com.geoshare.backend.parents.Ownable;
import com.geoshare.backend.security.SecurityUser;

public class HelperService {
	
	public static <T extends Ownable> boolean userOwns (Authentication auth, Collection<T> ownedOrNotThings) {
		
		//System.out.println(auth.getPrincipal() instanceof Jwt ? "jwt" : "not jwt");
		//Long userID = ((SecurityUser) auth.getPrincipal()).getUserId();
		
		String username = ( (Jwt) auth.getPrincipal()).getSubject();
		
		System.out.println(username);
		
		return ownedOrNotThings.stream()
				.allMatch(location -> location.getUser().getUsername().equalsIgnoreCase(username));
	}
}
