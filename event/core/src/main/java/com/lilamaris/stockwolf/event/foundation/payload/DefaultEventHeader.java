package com.lilamaris.stockwolf.event.foundation.payload;

import com.lilamaris.stockwolf.event.core.payload.EventHeader;

import java.time.Instant;

public record DefaultEventHeader(
        String eventKey,
        Instant occurredAt
) implements EventHeader {
}
