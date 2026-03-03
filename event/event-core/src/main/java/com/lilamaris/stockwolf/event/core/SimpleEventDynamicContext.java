package com.lilamaris.stockwolf.event.core;

import org.jspecify.annotations.Nullable;

public record SimpleEventDynamicContext(
        String aggregateType,
        String aggregateId,
        @Nullable String correlationId,
        @Nullable String causationId
) implements EventDynamicContext {
}
