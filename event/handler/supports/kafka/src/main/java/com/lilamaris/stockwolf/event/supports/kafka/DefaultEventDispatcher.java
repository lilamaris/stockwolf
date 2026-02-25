package com.lilamaris.stockwolf.event.supports.kafka;

import com.lilamaris.stockwolf.event.core.EventPayload;

public class DefaultEventDispatcher implements EventDispatcher {
    private final EventCodec codec;
    private final EventHandlerRegistry registry;

    public DefaultEventDispatcher(
            EventCodec codec,
            EventHandlerRegistry registry
    ) {
        this.codec = codec;
        this.registry = registry;
    }

    @Override
    public void dispatch(String raw) throws Exception {
        DecodedEvent decoded = codec.decode(raw);
        
        String eventKey = decoded.eventKey();
        EventHandler<?> handler = registry.resolve(eventKey)
                .orElseThrow(() -> new IllegalArgumentException("Not support event key: " + eventKey));

        invoke(handler, decoded);
    }

    @SuppressWarnings("unchecked")
    private <P extends EventPayload> void invoke(EventHandler<P> handler, DecodedEvent decoded) throws Exception {
        if (!handler.payloadType().isInstance(decoded.payload())) {
            throw new IllegalStateException(String.format(
                    "Handle payload type mismatch. key=%s, expected=%s, actual=%s",
                    decoded.eventKey(),
                    handler.payloadType().getName(),
                    decoded.payload().getClass().getName()
            ));
        }

        handler.handle(decoded, (P) decoded.payload());
    }

}
