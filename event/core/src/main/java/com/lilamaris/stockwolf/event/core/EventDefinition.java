package com.lilamaris.stockwolf.event.core;

public interface EventDefinition<P extends EventPayload> {
    String key();

    Class<P> payload();
}
