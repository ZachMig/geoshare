package com.geoshare.backend.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
            		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .authorizeHttpRequests((authorizeHttpRequests) ->
                            authorizeHttpRequests
                            		.requestMatchers("/api/users/create").permitAll()
                            		.requestMatchers("/api/auth/gettoken").permitAll()
                            		.requestMatchers("/api/countries/findall").permitAll()
                            		.requestMatchers("/api/metas/findall").permitAll()
                            		.requestMatchers("/api/users/findall").permitAll()
                            		.anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .oauth2ResourceServer((oauth) -> oauth.jwt((jwt) -> jwt.decoder(jwtDecoder())));
            return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

    	CorsConfiguration corsConfig = new CorsConfiguration();
    	UrlBasedCorsConfigurationSource urlCorsConfig = new UrlBasedCorsConfigurationSource();
    	
    	corsConfig.setAllowCredentials(true);
    	corsConfig.setAllowedOriginPatterns(Arrays.asList("http://localhost:*"));
    	corsConfig.setAllowedHeaders(Arrays.asList("authorization", "content-type"));
    	corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    	
        urlCorsConfig.registerCorsConfiguration("/**", corsConfig);
        
        return urlCorsConfig;
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
    JwtDecoder jwtDecoder() {
    	try {
    		return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    	} catch (JOSEException e) {
    		throw new RuntimeException("JwtDecoder bean creation error", e);
    	}
    }
    
}









