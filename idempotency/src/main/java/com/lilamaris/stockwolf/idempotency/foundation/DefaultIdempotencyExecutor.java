package com.lilamaris.stockwolf.idempotency.foundation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyExecutor;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;
import com.lilamaris.stockwolf.idempotency.core.IdempotencyResult;
import com.lilamaris.stockwolf.idempotency.core.exception.*;
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
    private final ObjectMapper mapper;

    public DefaultIdempotencyExecutor(
            IdempotencyStore store,
            IdempotencyCache idempotencyCache,
            IdempotencyCacheKeyBuilder cacheKeyBuilder,
            Duration cacheTtl,
            Duration executionTtl,
            ObjectMapper mapper
    ) {
        this.store = store;
        this.cache = idempotencyCache;
        this.cacheKeyBuilder = cacheKeyBuilder;
        this.cacheTtl = cacheTtl;
        this.executionTtl = executionTtl;
        this.mapper = mapper;
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
            Class<T> expectReturnType
    ) {
        var cacheKey = cacheKeyBuilder.buildKey(key, context);
        var cachedResult = cache.get(cacheKey);

        if (cachedResult != null) return new IdempotencyResult.Cached<>(expectReturnType.cast(cachedResult));

        var processingResult = store.ensureExistAndGet(key);

        if (processingResult.isFail()) {
            String failReason = processingResult.failReason().orElseThrow(() -> new IdempotencyProcessingFailedException(String.format(
                    "Idempotency key '%s' is already report failed. but fail reason not commented.", key
            )));
            throw new IdempotencyExecutionFailedException(failReason);
        }

        if (processingResult.isComplete()) {
            String stringifyResult = processingResult.stringifyResult().orElseThrow(() -> new IdempotencyProcessingFailedException(String.format(
                    "Idempotency key '%s' is already completed. but execution result not exists.", key
            )));
            T result = materialize(stringifyResult, expectReturnType);
            cache.put(cacheKey, result, cacheTtl);
            return new IdempotencyResult.Cached<>(result);
        }

        if (processingResult.isProgress()) {
            var processingContext = processingResult.context().orElseThrow(() -> new IdempotencyProcessingFailedException(String.format(
                    "Idempotency key '%s' is currently progressing. but execution context not exists.", key
            )));
            if (!context.match(processingContext)) throw new IdempotencyContextConflictException();

            var progressAt = processingResult.progressAt();
            var timeoutAt = progressAt.plus(executionTtl);
            if (Instant.now().isAfter(timeoutAt)) {
                store.retryable(key);
            }
            throw new IdempotencyPendingException();
        }

        if (processingResult.isReady() || processingResult.isRetryable()) {
            boolean acquired = store.progress(key, context);
            if (!acquired) {
                throw new IdempotencyPendingException();
            }

            try {
                T result = action.get();
                store.complete(key, stringify(result));
                cache.put(cacheKey, result, cacheTtl);
                return new IdempotencyResult.Executed<>(result);
            } catch (Exception e) {
                store.fail(key);
                throw new IdempotencyExecutionFailedException(e.getMessage());
            }
        }
        throw new IllegalStateException("Unknown status");
    }

    private <T> T materialize(String stringifyResult, Class<T> expectResultType) {
        try {
            return mapper.readValue(stringifyResult, expectResultType);
        } catch (JsonProcessingException e) {
            log.error("Idempotency executor throw error while materialize stringify stringifyResult: ", e);
            throw new IdempotencyException("Stringify stringifyResult materialize failed.");
        }
    }

    private String stringify(Object result) {
        try {
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.error("Idempotency executor throw error while stringify stringifyResult: ", e);
            throw new IdempotencyException("Result stringify failed.");
        }
    }
}
