package com.lilamaris.stockwolf.idempotency.core;

public interface IdempotencyResult<T> {
    T result();

    record Executed<T>(T result) implements IdempotencyResult<T> {
    }

    record Cached<T>(T result) implements IdempotencyResult<T> {
    }
}
