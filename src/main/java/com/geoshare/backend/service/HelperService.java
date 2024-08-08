package com.geoshare.backend.service;

import java.util.Collection;

import org.springframework.security.core.Authentication;

import com.geoshare.backend.parents.Ownable;
import com.geoshare.backend.security.SecurityUser;

public class HelperService {
	
	public static <T extends Ownable> boolean userOwns (Authentication auth, Collection<T> ownedOrNotThings) {
		Long userID = ((SecurityUser) auth.getPrincipal()).getUserId();
		
		return ownedOrNotThings.stream()
				.allMatch(location -> location.getUser().getId().equals(userID));
	}
}
