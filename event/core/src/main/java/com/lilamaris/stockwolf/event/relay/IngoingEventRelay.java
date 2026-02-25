package com.lilamaris.stockwolf.event.relay;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.payload.EventHeader;
import com.lilamaris.stockwolf.event.core.payload.EventPayload;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.foundation.EventDefinitionRegistry;

public class IngoingEventRelay {
    private final EventDefinitionRegistry registry;
    private final EventCodec codec;

    public IngoingEventRelay(
            EventDefinitionRegistry registry,
            EventCodec codec
    ) {
        this.registry = registry;
        this.codec = codec;
    }

    public void invoke(String raw) throws Exception {
        EventHeader header = codec.decodeHeader(raw);

        String eventKey = header.eventKey();

        var definition = registry.resolve(eventKey)
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "Not support event key: %s",
                        eventKey
                )));

        invokeTyped(definition, raw);
    }

    private <P extends EventPayload> void invokeTyped(EventDefinition<P> def, String raw) throws Exception {
        P payload = codec.decodePayload(raw, def.payload());
        def.handle(payload);
    }
}
