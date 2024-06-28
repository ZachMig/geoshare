package com.geoshare.backend.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.springframework.stereotype.Component;

/**
 * Code by Joe Grandja from the Spring Security team
 */
@Component
public class KeyGeneratorUtils {

	private KeyGeneratorUtils() {}
	
	static KeyPair generateRsaKey() {
		KeyPair keyPair;
		
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		
		return keyPair;
	}
	
}
