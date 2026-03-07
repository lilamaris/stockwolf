package com.lilamaris.stockwolf.event.core;

public record SimpleEventEnvelope(
        SimpleEventHeader header,
        SimpleEventTrace trace,
        String rawPayload
) implements EventEnvelope {
}
