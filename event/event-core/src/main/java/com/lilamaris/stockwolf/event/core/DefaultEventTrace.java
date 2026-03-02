package com.lilamaris.stockwolf.event.core;

public record DefaultEventTrace(
        String eventId,
        String correlationId,
        String causationId,
        String producer,
        String aggregateType,
        String aggregateId
) implements EventTrace {
}
