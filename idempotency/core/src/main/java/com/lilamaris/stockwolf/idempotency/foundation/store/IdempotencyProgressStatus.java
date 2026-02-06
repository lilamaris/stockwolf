package com.lilamaris.stockwolf.idempotency.foundation.store;

public enum IdempotencyProgressStatus {
    READY,
    IN_PROGRESS,
    COMPLETE,
    FAIL,
    RETRYABLE
}
