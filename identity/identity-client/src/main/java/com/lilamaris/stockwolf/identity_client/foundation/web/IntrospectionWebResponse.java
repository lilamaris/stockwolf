package com.lilamaris.stockwolf.identity_client.foundation.web;

import com.lilamaris.stockwolf.identity_client.core.Introspection;
import jakarta.annotation.Nullable;

public record IntrospectionWebResponse(
        Boolean active,
        @Nullable String subject,
        @Nullable String scope,
        @Nullable Long exp
) implements Introspection {
}
