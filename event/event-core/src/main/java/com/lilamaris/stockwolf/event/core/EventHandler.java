package com.lilamaris.stockwolf.event.core;

public interface EventHandler<P extends EventPayload> {
    EventKey key();

    Class<P> payload();

    void handle(P payload);
}
