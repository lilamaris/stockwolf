package com.lilamaris.stockwolf.identity.application.port.in;

import java.util.UUID;

public interface TokenIssuer {
    TokenEntry issue(UUID userId);
}
