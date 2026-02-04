package com.lilamaris.stockwolf.idempotency.core;

public interface IdempotencyKey {
    String subject();

    String op();
}
