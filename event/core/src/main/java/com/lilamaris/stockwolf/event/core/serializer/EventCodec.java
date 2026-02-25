package com.lilamaris.stockwolf.event.core.serializer;

import com.lilamaris.stockwolf.event.core.payload.EventEnvelope;
import com.lilamaris.stockwolf.event.core.payload.EventHeader;
import com.lilamaris.stockwolf.event.core.payload.EventPayload;
import com.lilamaris.stockwolf.event.core.payload.EventTrace;

public interface EventCodec {
    EventHeader decodeHeader(String raw);

    EventTrace decodeTrace(String raw);

    <P extends EventPayload> P decodePayload(String raw, Class<P> payloadType);

    String encode(EventHeader header, EventTrace trace, EventPayload payload);

    default String encode(EventEnvelope<? extends EventPayload> envelope) {
        return encode(envelope.header(), envelope.trace(), envelope.payload());
    }
}
