package com.lilamaris.stockwolf.event.core;

public record DefaultEventEnvelope(
        EventHeader header,
        EventTrace trace,
        String raw
) implements EventEnvelope {
}
