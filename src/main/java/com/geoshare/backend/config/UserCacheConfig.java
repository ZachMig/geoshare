package com.geoshare.backend.config;

import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;

@Configuration
public class UserCacheConfig {

    @Bean
    public UserCache userCache() {
        return new SpringCacheBasedUserCache(new ConcurrentMapCache("userCache"));
    }
	
}
