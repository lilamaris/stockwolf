package com.lilamaris.stockwolf.event.core.registry;

import com.lilamaris.stockwolf.event.core.EventPayload;

public interface EventDefinition<Payload extends EventPayload> {
    String key();

    Class<Payload> payload();
}
