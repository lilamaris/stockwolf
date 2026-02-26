package com.lilamaris.stockwolf.idempotency.core.exception;

public class IdempotencyContextConflictException extends IdempotencyException {
    public IdempotencyContextConflictException() {
        super("Idempotency processing context mismatch.");
    }
}
