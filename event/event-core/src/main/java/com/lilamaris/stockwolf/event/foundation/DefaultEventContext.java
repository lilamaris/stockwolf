package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventContext;
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
