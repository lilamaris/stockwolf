package com.lilamaris.stockwolf.idempotency.core.store;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;

public interface IdempotencyCacheKeyBuilder {
    String buildKey(
            IdempotencyKey key,
            IdempotencyContext context
    );
}
