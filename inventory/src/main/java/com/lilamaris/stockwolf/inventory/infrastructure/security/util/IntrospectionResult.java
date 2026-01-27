package com.lilamaris.stockwolf.inventory.infrastructure.security.util;

public record IntrospectionResult(
        Boolean active,
        Summary summary
) {
    public record Summary(
            String subject,
            String scope,
            Long expiration
    ) {
    }
}
