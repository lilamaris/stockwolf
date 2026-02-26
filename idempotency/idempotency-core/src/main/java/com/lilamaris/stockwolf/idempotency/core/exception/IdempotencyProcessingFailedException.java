package com.lilamaris.stockwolf.idempotency.core.exception;

public class IdempotencyProcessingFailedException extends IdempotencyException {
    public IdempotencyProcessingFailedException(String message) {
        super(message);
    }
}
