package com.lilamaris.stockwolf.event.foundation.relay;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.foundation.EventRegistry;

public class InboundRelay {
    private final EventRegistry registry;
    private final EventCodec codec;

    public InboundRelay(
            EventRegistry registry,
            EventCodec codec
    ) {
        this.registry = registry;
        this.codec = codec;
    }

    public void invoke(String raw) throws Exception {
        EventHeader header = codec.decodeHeader(raw);

        var eventKey = header.eventKey();

        var binding = registry.resolve(eventKey)
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "Not support event name=%s",
                        eventKey.name()
                )));

        binding.decodeAndHandle(raw);
    }
}
