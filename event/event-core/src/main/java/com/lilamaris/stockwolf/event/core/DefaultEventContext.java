package com.lilamaris.stockwolf.event.core;

import org.jspecify.annotations.Nullable;

public record DefaultEventContext(
        String aggregateType,
        String aggregateId,
        @Nullable String correlationId,
        @Nullable String causationId
) implements EventContext {
    public boolean hasCorrelationId() {
        return correlationId != null && !correlationId.isBlank();
    }

    public boolean hasCausationId() {
        return causationId != null && !causationId.isBlank();
    }
}
