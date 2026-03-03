package com.lilamaris.stockwolf.event.core;

import org.jspecify.annotations.Nullable;

public record SimpleEventTrace(
        String eventId,
        String correlationId,
        @Nullable String causationId,
        String producer,
        String aggregateType,
        String aggregateId
) implements EventTrace {
    public static SimpleEventTrace of(EventTrace eventTrace) {
        return new SimpleEventTrace(
                eventTrace.eventId(),
                eventTrace.correlationId(),
                eventTrace.causationId(),
                eventTrace.producer(),
                eventTrace.aggregateType(),
                eventTrace.aggregateId()
        );
    }
}
