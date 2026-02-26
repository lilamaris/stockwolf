package com.lilamaris.stockwolf.identity_client.foundation.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.introspection")
public record IntrospectionClientProperties(
        Server server,
        String api
) {
    public record Server(String baseUrl) {
    }
}
