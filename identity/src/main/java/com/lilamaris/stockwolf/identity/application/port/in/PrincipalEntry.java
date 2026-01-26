package com.lilamaris.stockwolf.identity.application.port.in;

import java.util.UUID;

public record PrincipalEntry(
        UUID userId
) {
}
