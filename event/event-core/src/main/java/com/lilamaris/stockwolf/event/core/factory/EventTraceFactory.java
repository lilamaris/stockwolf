package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.EventTrace;
import org.jspecify.annotations.Nullable;

public interface EventTraceFactory {
    EventTrace build(
            String aggregateType,
            String aggregateId,
            @Nullable String correlationId,
            @Nullable String causationId
    );
}
