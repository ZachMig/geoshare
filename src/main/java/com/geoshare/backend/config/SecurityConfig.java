package com.geoshare.backend.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.geoshare.backend.security.JPAUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    private final JPAUserDetailsService jpaUserDetailsService;
    
    public SecurityConfig(JPAUserDetailsService jpaUserDetailsService) {
    	this.jpaUserDetailsService = jpaUserDetailsService;
    }
    
//    @Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		return http
//				.authorizeRequests( auth -> auth
//						.anyRequest().authenticated()
//				)
//				.userDetailsService(jpaUserDetailsService)
//				.formLogin(withDefaults())
//				.build();
//	}
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
            		.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests((authorizeHttpRequests) ->
                            authorizeHttpRequests
                            		.requestMatchers("/api/users/create").permitAll()
                            		.requestMatchers("api/locations/create").permitAll()
                                    //.requestMatchers("/**").hasRole("USER")
                            		.anyRequest().authenticated()
                    )
                    //.formLogin(withDefaults());
                    .httpBasic(withDefaults());
            return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

}