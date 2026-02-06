package com.lilamaris.stockwolf.idempotency.core.exception;

public class IdempotencyPendingException extends IdempotencyException {
    public IdempotencyPendingException() {
        super("처리 중인 작업입니다.");
    }
}
