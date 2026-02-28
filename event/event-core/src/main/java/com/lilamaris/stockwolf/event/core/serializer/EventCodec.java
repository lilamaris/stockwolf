package com.lilamaris.stockwolf.event.core.serializer;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;

public interface EventCodec {
    EventHeader decodeHeader(String raw);

    EventTrace decodeTrace(String raw);

    <P extends EventPayload> P decodePayload(String raw, Class<P> payloadType);

    <P extends EventPayload> String encodePayload(P payload);

    String encode(EventHeader header, EventTrace trace, String raw);
}
