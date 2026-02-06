package com.lilamaris.stockwolf.idempotency.foundation;

import com.lilamaris.stockwolf.idempotency.core.IdempotencyKey;

public record MappedIdempotencyKey(
        String subject,
        String op
) implements IdempotencyKey {
}
