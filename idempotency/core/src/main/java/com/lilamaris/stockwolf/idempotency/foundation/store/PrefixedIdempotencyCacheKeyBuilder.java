package com.lilamaris.stockwolf.idempotency.foundation.store;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCacheKeyBuilder;

import java.util.Arrays;
import java.util.List;

public class PrefixedIdempotencyCacheKeyBuilder implements IdempotencyCacheKeyBuilder {
    private final String prefix;

    public PrefixedIdempotencyCacheKeyBuilder(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String buildKey(
            IdempotencyKey key,
            IdempotencyContext context
    ) {
        List<String> candidates = Arrays.asList(prefix, key.subject(), key.op(), context.hash());
        return String.join(":", candidates);
    }
}
