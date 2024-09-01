package com.geoshare.backend.config;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapsApiCountConfig {

	@Value("${MAPS_API_COUNT}")
	private int initialApiCount;
	
	@Bean
	public AtomicInteger apiCount() {
		return new AtomicInteger(initialApiCount);
	}
}
