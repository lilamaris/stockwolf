package com.lilamaris.stockwolf.identity_client.web;

import com.lilamaris.stockwolf.identity_client.core.Introspection;
import com.lilamaris.stockwolf.identity_client.core.Introspector;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
public class IntrospectorWebClient implements Introspector {
    private final WebClient introspectionClient;
    private final String apiUri;

    @Override
    public Introspection introspect(String token) {
        return introspectionClient.post()
                .uri(apiUri)
                .bodyValue(Map.of("token", token))
                .retrieve()
                .bodyToMono(IntrospectionWebResponse.class)
                .block();
    }
}
