package com.lilamaris.stockwolf.inventory.application.port.out;

import java.util.Set;

public record Actor(
        String subject,
        Set<String> scopes
) {
}
