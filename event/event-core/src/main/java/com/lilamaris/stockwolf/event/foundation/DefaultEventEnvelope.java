package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;

public record DefaultEventEnvelope<P extends EventPayload>(
        EventHeader header,
        P payload,
        EventTrace trace
) implements EventEnvelope<P> {
}
