package com.lilamaris.stockwolf.event.foundation.payload;

import com.lilamaris.stockwolf.event.core.payload.EventEnvelope;
import com.lilamaris.stockwolf.event.core.payload.EventHeader;
import com.lilamaris.stockwolf.event.core.payload.EventPayload;
import com.lilamaris.stockwolf.event.core.payload.EventTrace;

public record DefaultEventEnvelope<P extends EventPayload>(
        EventHeader header,
        P payload,
        EventTrace trace
) implements EventEnvelope<P> {
}
