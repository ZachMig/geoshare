package com.geoshare.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "maps.api")
public class ApiProps {

	private String key;
	private String secret;
	
}
