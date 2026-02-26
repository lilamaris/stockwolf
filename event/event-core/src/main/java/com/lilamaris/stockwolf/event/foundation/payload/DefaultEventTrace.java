package com.lilamaris.stockwolf.event.foundation.payload;

import com.lilamaris.stockwolf.event.core.payload.EventTrace;

public record DefaultEventTrace(
        String eventId,
        String correlationId,
        String causationId,
        String producer,
        String aggregateType,
        String aggregateId
) implements EventTrace {
}
