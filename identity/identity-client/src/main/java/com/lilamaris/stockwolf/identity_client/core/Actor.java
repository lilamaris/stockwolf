package com.lilamaris.stockwolf.identity_client.core;

import java.util.Set;

public record Actor(
        String subject,
        Set<String> scopes
) {
}
