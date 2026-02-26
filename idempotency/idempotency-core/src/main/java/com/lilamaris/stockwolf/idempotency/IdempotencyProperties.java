package com.lilamaris.stockwolf.idempotency;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "spring.idempotency")
public record IdempotencyProperties(Cache cache, Duration executionTtl) {
    public record Cache(String keyPrefix, Duration ttl) {
    }
}
