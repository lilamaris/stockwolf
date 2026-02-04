package com.lilamaris.stockwolf.idempotency.foundation.store;

import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCache;

import java.time.Duration;

public class NoOpIdempotencyCache implements IdempotencyCache {
    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public void put(String key, Object value, Duration ttl) {

    }
}
