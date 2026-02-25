package com.lilamaris.stockwolf.event.core.payload;

import org.jspecify.annotations.Nullable;

public interface EventTrace {
    String eventId();

    String correlationId();

    @Nullable String causationId();

    String producer();

    String aggregateType();

    String aggregateId();
}
