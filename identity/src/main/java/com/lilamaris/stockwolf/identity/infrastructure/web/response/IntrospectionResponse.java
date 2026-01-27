package com.lilamaris.stockwolf.identity.infrastructure.web.response;

import jakarta.annotation.Nullable;

public record IntrospectionResponse(
        Boolean active,
        @Nullable Summary summary
) {
    public static IntrospectionResponse active(String subject, String scope, Long expiration) {
        var summary = new Summary(subject, scope, expiration);
        return new IntrospectionResponse(true, summary);
    }

    public static IntrospectionResponse inactive() {
        return new IntrospectionResponse(false, null);
    }

    public record Summary(
            String subject,
            String scope,
            Long expiration
    ) {
    }
}
