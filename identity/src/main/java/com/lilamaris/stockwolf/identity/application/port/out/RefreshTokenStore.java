package com.lilamaris.stockwolf.identity.application.port.out;

import java.time.Duration;

public interface RefreshTokenStore {
    String consume(String key);

    void save(String key, String value, Duration ttl);
}
