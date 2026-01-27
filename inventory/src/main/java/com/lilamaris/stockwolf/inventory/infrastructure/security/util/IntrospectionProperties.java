package com.lilamaris.stockwolf.inventory.infrastructure.security.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.introspection")
public record IntrospectionProperties(Server server, String api) {
    public record Server(String baseUrl) {
    }
}
