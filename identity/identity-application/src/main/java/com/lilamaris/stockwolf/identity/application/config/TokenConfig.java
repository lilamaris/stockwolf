package com.lilamaris.stockwolf.identity.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TokenConfig {
    @Bean
    Duration accessTokenExpiration(
            @Value("${spring.security.jwt.accessTokenExpiration}")
            Duration expiration
    ) {
        return expiration;
    }

    @Bean
    Duration refreshTokenExpiration(
            @Value("${spring.security.jwt.refreshTokenExpiration}")
            Duration expiration
    ) {
        return expiration;
    }
}
