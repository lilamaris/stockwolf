package com.lilamaris.stockwolf.idempotency.foundation;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyContext;

import java.util.Objects;

public class MappedIdempotencyContext implements IdempotencyContext {
    private final String hash;

    public MappedIdempotencyContext(String hash) {
        this.hash = hash;
    }

    public static MappedIdempotencyContext from(Object... values) {
        var hash = Integer.toHexString(Objects.hash(values));
        return new MappedIdempotencyContext(hash);
    }

    public static MappedIdempotencyContext noContext() {
        return new MappedIdempotencyContext("");
    }

    @Override
    public String hash() {
        return hash;
    }
}
