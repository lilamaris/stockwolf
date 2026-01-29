package com.lilamaris.stockwolf.inventory.infrastructure.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
public class Introspector {
    private final WebClient introspectionClient;
    private final String apiUri;

    public IntrospectionResult introspect(String token) {
        return introspectionClient.post()
                .uri(apiUri)
                .bodyValue(Map.of("token", token))
                .retrieve()
                .bodyToMono(IntrospectionResult.class)
                .block();
    }
}
