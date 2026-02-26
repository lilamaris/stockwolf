package com.lilamaris.stockwolf.event.core.payload;

import java.time.Instant;

public interface EventHeader {
    String eventKey();

    Instant occurredAt();
}
