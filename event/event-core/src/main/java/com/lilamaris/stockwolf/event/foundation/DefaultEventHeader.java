package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventKey;

import java.time.Instant;

public record DefaultEventHeader(
        EventKey eventKey,
        Instant occurredAt
) implements EventHeader {
    public static DefaultEventHeader of(String eventKeyName, Instant occurredAt) {
        return new DefaultEventHeader(DefaultEventKey.of(eventKeyName), occurredAt);
    }
}
