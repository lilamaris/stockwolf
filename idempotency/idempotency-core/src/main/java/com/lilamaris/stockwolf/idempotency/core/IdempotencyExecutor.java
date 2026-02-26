package com.lilamaris.stockwolf.idempotency.core;

import java.util.function.Supplier;

public interface IdempotencyExecutor {
    <T> IdempotencyResult<T> execute(
            IdempotencyKey key,
            IdempotencyContext context,
            Supplier<T> action,
            Class<T> expect
    );
}
