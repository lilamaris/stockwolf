package com.lilamaris.stockwolf.event.core;

import java.time.Instant;

public interface EventEnvelope<P extends EventPayload> {
    String eventKey();

    Instant occurredAt();

    P payload();

    EventTrace trace();
}
