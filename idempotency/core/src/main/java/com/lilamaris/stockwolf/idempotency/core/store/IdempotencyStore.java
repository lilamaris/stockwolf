package com.lilamaris.stockwolf.idempotency.core.store;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import com.lilamaris.stockwolf.idempotency.core.Idempotent;

import java.util.Optional;

public interface IdempotencyStore {
    Idempotent ensureExistAndGet(IdempotencyKey key);

    <T> Optional<T> resolveResult(Idempotent idempotent, Class<T> expect);

    boolean complete(IdempotencyKey key, Object result);

    boolean progress(IdempotencyKey key, IdempotencyContext context);

    boolean fail(IdempotencyKey key);

    boolean retryable(IdempotencyKey key);
}
