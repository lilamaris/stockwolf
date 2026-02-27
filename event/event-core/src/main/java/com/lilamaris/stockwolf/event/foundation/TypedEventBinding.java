package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.*;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;

public class TypedEventBinding<P extends EventPayload> implements EventBinding {
    private final EventDefinition<P> definition;
    private final EventHandler<P> handler;
    private final EventCodec codec;

    public TypedEventBinding(
            EventDefinition<P> definition,
            EventHandler<P> handler,
            EventCodec codec
    ) {
        this.definition = definition;
        this.handler = handler;
        this.codec = codec;
    }

    @Override
    public EventKey key() {
        return definition.key();
    }

    @Override
    public void decodeAndHandle(String raw) throws Exception {
        P payload = codec.decodePayload(raw, definition.payload());
        handler.handle(payload);
    }

    public EventDefinition<P> getDefinition() {
        return definition;
    }

    public EventHandler<P> getHandler() {
        return handler;
    }
}
