package com.lilamaris.stockwolf.idempotency.core.exception;

public class IdempotencyExecutionFailedException extends IdempotencyException {
    public IdempotencyExecutionFailedException(String message) {
        super(message);
    }
}
