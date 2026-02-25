package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;

import java.time.Instant;

public record DefaultEventEnvelope<P extends EventPayload>(
        String eventKey,
        Instant occurredAt,
        P payload,
        EventTrace trace
) implements EventEnvelope<P> {
}
