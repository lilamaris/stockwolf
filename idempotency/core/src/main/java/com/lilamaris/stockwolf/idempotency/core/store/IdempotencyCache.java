package com.lilamaris.stockwolf.idempotency.core.store;

import java.time.Duration;

public interface IdempotencyCache {
    Object get(String key);

    void put(String key, Object value, Duration ttl);
}
