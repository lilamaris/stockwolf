package com.lilamaris.stockwolf.idempotency.core.store;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyProcessingResult;

public interface IdempotencyStore {
    IdempotencyProcessingResult ensureExistAndGet(IdempotencyKey key);

    boolean complete(IdempotencyKey key, String result);

    boolean progress(IdempotencyKey key, IdempotencyContext context);

    boolean fail(IdempotencyKey key);

    boolean retryable(IdempotencyKey key);
}
