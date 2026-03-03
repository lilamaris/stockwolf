package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.EventDynamicContext;
import com.lilamaris.stockwolf.event.core.SimpleEventDynamicContext;
import org.jspecify.annotations.Nullable;

public class SimpleEventDynamicContextFactory implements EventDynamicContextFactory {
    @Override
    public EventDynamicContext build(String aggregateType, String aggregateId, @Nullable String correlationId, @Nullable String causationId) {
        return new SimpleEventDynamicContext(
                aggregateType,
                aggregateId,
                correlationId,
                causationId
        );
    }
}
