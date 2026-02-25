package com.lilamaris.stockwolf.event.supports.kafka;

import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;

import java.time.Instant;

public record DecodedEvent(
        String eventKey,
        Instant occurredAt,
        EventTrace trace,
        EventPayload payload,
        String rawJson
) {
}
