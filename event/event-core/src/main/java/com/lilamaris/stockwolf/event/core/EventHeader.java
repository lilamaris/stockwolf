package com.lilamaris.stockwolf.event.core;

import java.time.Instant;

public interface EventHeader {
    EventKey eventKey();

    Instant occurredAt();
}
