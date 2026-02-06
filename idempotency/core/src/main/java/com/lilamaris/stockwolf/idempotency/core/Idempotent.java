package com.lilamaris.stockwolf.idempotency.core;

import com.lilamaris.stockwolf.idempotency.foundation.store.IdempotencyProgressStatus;

import java.time.Instant;
import java.util.Optional;

public interface Idempotent {
    Optional<IdempotencyContext> context();

    Optional<Object> rawResult();

    Optional<String> failReason();

    IdempotencyProgressStatus status();

    Instant progressAt();

    default boolean isReady() {
        return status() == IdempotencyProgressStatus.READY;
    }

    default boolean isComplete() {
        return status() == IdempotencyProgressStatus.COMPLETE;
    }

    default boolean isProgress() {
        return status() == IdempotencyProgressStatus.IN_PROGRESS;
    }

    default boolean isFail() {
        return status() == IdempotencyProgressStatus.FAIL;
    }

    default boolean isRetryable() {
        return status() == IdempotencyProgressStatus.RETRYABLE;
    }
}

