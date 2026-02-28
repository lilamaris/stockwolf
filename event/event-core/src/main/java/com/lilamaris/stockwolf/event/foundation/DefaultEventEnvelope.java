package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventTrace;

public record DefaultEventEnvelope(
        EventHeader header,
        EventTrace trace,
        String raw
) implements EventEnvelope {
}
