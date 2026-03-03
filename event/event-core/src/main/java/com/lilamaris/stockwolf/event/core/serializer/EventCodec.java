package com.lilamaris.stockwolf.event.core.serializer;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventTrace;

public interface EventCodec {
    EventEnvelope decode(String raw);

    String encode(EventHeader header, EventTrace trace, String rawPayload);
}
