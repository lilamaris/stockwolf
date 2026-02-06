package com.lilamaris.stockwolf.idempotency.foundation;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyExecutor;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyResult;
import com.lilamaris.stockwolf.idempotency.core.exception.IdempotencyContextConflictException;
import com.lilamaris.stockwolf.idempotency.core.exception.IdempotencyExecutionFailedException;
import com.lilamaris.stockwolf.idempotency.core.exception.IdempotencyPendingException;
import com.lilamaris.stockwolf.idempotency.core.exception.IdempotencyProcessingFailedException;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCache;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyCacheKeyBuilder;
import com.lilamaris.stockwolf.idempotency.core.store.IdempotencyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.resilience.annotation.Retryable;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class DefaultIdempotencyExecutor implements IdempotencyExecutor {
    private static final Logger log = LoggerFactory.getLogger(DefaultIdempotencyExecutor.class);
    private final IdempotencyStore store;
    private final IdempotencyCache cache;
    private final IdempotencyCacheKeyBuilder cacheKeyBuilder;
    private final Duration cacheTtl;
    private final Duration executionTtl;

    public DefaultIdempotencyExecutor(
            IdempotencyStore store,
            IdempotencyCache idempotencyCache,
            IdempotencyCacheKeyBuilder cacheKeyBuilder,
            Duration cacheTtl,
            Duration executionTtl
    ) {
        this.store = store;
        this.cache = idempotencyCache;
        this.cacheKeyBuilder = cacheKeyBuilder;
        this.cacheTtl = cacheTtl;
        this.executionTtl = executionTtl;
    }

    @Retryable(
            includes = IdempotencyPendingException.class,
            maxRetries = 10,
            delay = 50,
            multiplier = 2,
            jitter = 10,
            maxDelay = 1000
    )
    @Override
    public <T> IdempotencyResult<T> execute(
            IdempotencyKey key,
            IdempotencyContext context,
            Supplier<T> action,
            Class<T> expect
    ) {
        var cacheKey = cacheKeyBuilder.buildKey(key, context);
        var cached = cache.get(cacheKey, expect);

        if (cached != null) return new IdempotencyResult.Cached<>(cached);

        var entry = store.ensureExistAndGet(key);

        if (entry.isFail()) {
            String failReason = entry.failReason().orElseThrow(() -> new IdempotencyProcessingFailedException(String.format(
                    "Idempotency key '%s' is already report failed. but fail reason not commented.", key
            )));
            throw new IdempotencyExecutionFailedException(failReason);
        }

        if (entry.isComplete()) {
            T result = store.resolveResult(entry, expect).orElseThrow(() -> new IdempotencyProcessingFailedException(String.format(
                    "Idempotency key '%s' is already completed. but execution result not exists.", key
            )));
            cache.put(cacheKey, result, cacheTtl);
            return new IdempotencyResult.Cached<>(result);
        }

        if (entry.isProgress()) {
            var processingContext = entry.context().orElseThrow(() -> new IdempotencyProcessingFailedException(String.format(
                    "Idempotency key '%s' is currently progressing. but execution context not exists.", key
            )));
            if (!context.match(processingContext)) throw new IdempotencyContextConflictException();

            var progressAt = entry.progressAt();
            var timeoutAt = progressAt.plus(executionTtl);
            if (Instant.now().isAfter(timeoutAt)) {
                store.retryable(key);
            }
            throw new IdempotencyPendingException();
        }

        if (entry.isReady() || entry.isRetryable()) {
            boolean acquired = store.progress(key, context);
            if (!acquired) {
                throw new IdempotencyPendingException();
            }

            try {
                T result = action.get();
                store.complete(key, result);
                cache.put(cacheKey, result, cacheTtl);
                return new IdempotencyResult.Executed<>(result);
            } catch (Exception e) {
                store.fail(key);
                throw new IdempotencyExecutionFailedException(e.getMessage());
            }
        }
        throw new IllegalStateException("Unknown status");
    }
}
