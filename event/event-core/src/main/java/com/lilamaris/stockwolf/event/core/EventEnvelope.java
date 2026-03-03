package com.lilamaris.stockwolf.event.core;

public interface EventEnvelope {
    EventHeader header();

    EventTrace trace();

    String rawPayload();
}
