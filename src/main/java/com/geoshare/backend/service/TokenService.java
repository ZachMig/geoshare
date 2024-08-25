package com.geoshare.backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.geoshare.backend.dto.LoginRequestDTO;
import com.geoshare.backend.dto.LoginResponseDTO;
import com.geoshare.backend.entity.GeoshareUser;

import lombok.extern.slf4j.Slf4j;


/**
 * Taken from Dan Vega's stuff, check there if issues in future
 */
@Slf4j
@Service
public class TokenService {
	
	private final JwtEncoder encoder;
	private final GeoshareUserService userService;
	private final AuthenticationManager authManager;

	public TokenService(JwtEncoder encoder, GeoshareUserService userService, AuthenticationManager authManager,
			PasswordEncoder passwordEncoder) {
		this.encoder = encoder;
		this.userService = userService;
		this.authManager = authManager;
	}

	public String generateToken(Authentication authentication) {
		
		Instant now = Instant.now();
		String scope = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plus(24, ChronoUnit.HOURS))
				.subject(authentication.getName())
				.claim("scope", scope)
				.build();
		
		return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
	}
	
	public LoginResponseDTO loginUser(LoginRequestDTO loginRequest) throws AuthenticationException {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.username(), 
						loginRequest.password()));
		String token = generateToken(authentication);
		GeoshareUser user = userService.findUserByUsernameOrThrow(loginRequest.username());
		return new LoginResponseDTO(token, user.getId(), user.getEmail());
	}

}
