package com.lilamaris.stockwolf.event.core;

import java.time.Instant;

public record SimpleEventHeader(
        EventKey eventKey,
        Instant occurredAt
) implements EventHeader {
    public static SimpleEventHeader of(EventHeader eventHeader) {
        return new SimpleEventHeader(eventHeader.eventKey(), eventHeader.occurredAt());
    }
}
