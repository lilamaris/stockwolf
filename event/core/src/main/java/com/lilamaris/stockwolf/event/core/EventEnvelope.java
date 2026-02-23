package com.lilamaris.stockwolf.event.core;

import java.time.Instant;

public interface EventEnvelope<Payload extends EventPayload> {
    String eventKey();

    Instant occurredAt();

    Payload payload();

    EventTrace trace();
}
