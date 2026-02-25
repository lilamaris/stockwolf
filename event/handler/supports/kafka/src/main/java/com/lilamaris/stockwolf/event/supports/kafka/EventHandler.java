package com.lilamaris.stockwolf.event.supports.kafka;

import com.lilamaris.stockwolf.event.core.EventPayload;

public interface EventHandler<P extends EventPayload> {
    String key();
    Class<P> payloadType();
    void handle(DecodedEvent event, P payload) throws Exception;
}
