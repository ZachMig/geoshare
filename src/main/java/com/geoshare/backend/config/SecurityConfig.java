package com.geoshare.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.geoshare.backend.security.JPAUserDetailsService;
import com.geoshare.backend.security.Jwks;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    private final JPAUserDetailsService jpaUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private RSAKey rsaKey;
    
    public SecurityConfig(JPAUserDetailsService jpaUserDetailsService, PasswordEncoder passwordEncoder) {
    	this.jpaUserDetailsService = jpaUserDetailsService;
    	this.passwordEncoder = passwordEncoder;
    }
    
    @Bean
    public AuthenticationManager authManager(JPAUserDetailsService jpaUserDetailsService) {
    	var authProvider = new DaoAuthenticationProvider();
    	authProvider.setUserDetailsService(jpaUserDetailsService);
    	authProvider.setPasswordEncoder(passwordEncoder);
    	return new ProviderManager(authProvider);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
            		.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests((authorizeHttpRequests) ->
                            authorizeHttpRequests
                            		.requestMatchers("/api/users/create").permitAll()
                            		.requestMatchers("/api/auth/gettoken").permitAll()
                            		.anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
            return http.build();
    }
    
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
    	rsaKey = Jwks.generateRsa();
    	JWKSet jwkSet = new JWKSet(rsaKey);
    	return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }
    
    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
    	return new NimbusJwtEncoder(jwks);
    }
    
    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
    	return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }
    
}









