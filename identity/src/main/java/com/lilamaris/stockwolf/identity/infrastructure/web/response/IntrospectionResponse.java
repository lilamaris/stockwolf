package com.lilamaris.stockwolf.identity.infrastructure.web.response;

import jakarta.annotation.Nullable;

public record IntrospectionResponse(
        Boolean active,
        @Nullable String subject,
        @Nullable String scope,
        @Nullable Long exp
) {
    public static IntrospectionResponse active(String subject, String scope, Long exp) {
        return new IntrospectionResponse(true, subject, scope, exp);
    }

    public static IntrospectionResponse inactive() {
        return new IntrospectionResponse(false, null, null, null);
    }
}
