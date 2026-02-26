package com.lilamaris.stockwolf.event.relay;

import com.lilamaris.stockwolf.event.core.payload.EventHeader;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.foundation.EventRegistry;

public class IngoingEventRelay {
    private final EventRegistry registry;
    private final EventCodec codec;

    public IngoingEventRelay(
            EventRegistry registry,
            EventCodec codec
    ) {
        this.registry = registry;
        this.codec = codec;
    }

    public void invoke(String raw) throws Exception {
        EventHeader header = codec.decodeHeader(raw);

        String eventKey = header.eventKey();

        var definition = registry.resolveDefinition(eventKey)
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "Not support event key: %s",
                        eventKey
                )));

        var handler = registry.resolveHandler(eventKey)
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "Not support event key: %s",
                        eventKey
                )));
    }
}
