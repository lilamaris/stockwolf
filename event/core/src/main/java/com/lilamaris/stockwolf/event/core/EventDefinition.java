package com.lilamaris.stockwolf.event.core;

import com.lilamaris.stockwolf.event.core.payload.EventPayload;

public interface EventDefinition<P extends EventPayload> {
    String key();

    Class<P> payload();

    void handle(P payload) throws Exception;
}
