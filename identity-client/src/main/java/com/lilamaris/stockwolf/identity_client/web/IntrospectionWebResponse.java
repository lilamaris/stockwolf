package com.lilamaris.stockwolf.identity_client.web;

import com.lilamaris.stockwolf.identity_client.core.Introspection;
import jakarta.annotation.Nullable;

public record IntrospectionWebResponse(
        Boolean active,
        @Nullable String subject,
        @Nullable String scope,
        @Nullable Long exp
) implements Introspection {
}
