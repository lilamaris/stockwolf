package com.lilamaris.stockwolf.idempotency.core;

public interface IdempotencyContext {
    String hash();

    default boolean match(IdempotencyContext ctx) {
        return hash().equals(ctx.hash());
    }
}
