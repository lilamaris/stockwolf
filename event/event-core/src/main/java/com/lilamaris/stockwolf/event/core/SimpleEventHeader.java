package com.lilamaris.stockwolf.event.core;

import java.time.Instant;

public record SimpleEventHeader(
        SimpleEventKey eventKey,
        Instant occurredAt
) implements EventHeader {
    public static SimpleEventHeader of(EventHeader eventHeader) {
        var simpleEventKey = SimpleEventKey.of(eventHeader.eventKey());
        return new SimpleEventHeader(simpleEventKey, eventHeader.occurredAt());
    }
}
