package com.geoshare.backend.config;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiscConfig {
	
	@Bean
	Pattern mapsUrlPattern() {
		//Check if the input URL is a valid Google Maps
		//Unsure at this time how to determine between regular Maps and Street View
		Pattern gMapsPattern = Pattern.compile(
				"^(https?:\\/\\/)?(www\\.)?google\\.com\\/maps\\/@.*$",
				Pattern.CASE_INSENSITIVE
				);
		return gMapsPattern;
	}
	
	

	
}
