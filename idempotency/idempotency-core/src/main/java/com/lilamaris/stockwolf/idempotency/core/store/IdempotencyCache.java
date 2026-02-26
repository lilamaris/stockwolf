package com.lilamaris.stockwolf.idempotency.core.store;

import java.time.Duration;

public interface IdempotencyCache {
    <T> T get(String key, Class<T> expect);

    void put(String key, Object value, Duration ttl);
}
