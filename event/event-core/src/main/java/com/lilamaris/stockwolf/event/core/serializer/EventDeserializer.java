package com.lilamaris.stockwolf.event.core.serializer;

import com.lilamaris.stockwolf.event.core.EventPayload;

public interface EventDeserializer {
    <P extends EventPayload> P materialize(String rawPayload, Class<P> payloadType);
}
