package com.lilamaris.stockwolf.event.core.relay.outgoing;

import com.lilamaris.stockwolf.event.core.payload.EventPayload;

public interface EventHandler<P extends EventPayload> {
    String key();

    void handle(P payload);
}
