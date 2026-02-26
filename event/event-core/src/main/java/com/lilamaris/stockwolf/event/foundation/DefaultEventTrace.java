package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventTrace;

public record DefaultEventTrace(
        String eventId,
        String correlationId,
        String causationId,
        String producer,
        String aggregateType,
        String aggregateId
) implements EventTrace {
}
