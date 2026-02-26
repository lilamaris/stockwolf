package com.lilamaris.stockwolf.event.core.payload;

public interface EventEnvelope<P extends EventPayload> {
    EventHeader header();

    P payload();

    EventTrace trace();
}
