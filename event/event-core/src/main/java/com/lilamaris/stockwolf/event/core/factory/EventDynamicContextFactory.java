package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.EventDynamicContext;
import org.jspecify.annotations.Nullable;

public interface EventDynamicContextFactory {
    EventDynamicContext build(
            String aggregateType,
            String aggregateId,
            @Nullable String correlationId,
            @Nullable String causationId
    );
}
