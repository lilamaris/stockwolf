package com.lilamaris.stockwolf.event.core;

public record SimpleEventEnvelope(
        EventHeader header,
        EventTrace trace,
        String rawPayload
) implements EventEnvelope {
}
