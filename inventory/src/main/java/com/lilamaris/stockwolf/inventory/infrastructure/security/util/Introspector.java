package com.lilamaris.stockwolf.inventory.infrastructure.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(IntrospectionProperties.class)
public class Introspector {
    private final WebClient introspectionClient;
    private final IntrospectionProperties properties;

    public IntrospectionResult introspect(String token) {
        return introspectionClient.post()
                .uri(properties.api())
                .bodyValue(Map.of("token", token))
                .retrieve()
                .bodyToMono(IntrospectionResult.class)
                .block();
    }
}
