package com.lilamaris.stockwolf.idempotency.foundation.store;

import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCache;

import java.time.Duration;

public class NoOpIdempotencyCache implements IdempotencyCache {
    @Override
    public <T> T get(String key, Class<T> expect) {
        return null;
    }

    @Override
    public void put(String key, Object value, Duration ttl) {

    }
}
