package com.lilamaris.stockwolf.inventory.infrastructure.security.config;

import com.lilamaris.stockwolf.inventory.infrastructure.security.util.IntrospectionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(IntrospectionProperties.class)
public class IntrospectionConfig {
    @Bean
    WebClient introspectionClient(
            IntrospectionProperties properties
    ) {
        return WebClient.builder().baseUrl(properties.server().baseUrl()).build();
    }
}
