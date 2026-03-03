package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventTrace;

public interface EventEnvelopeFactory {
    EventEnvelope build(EventHeader eventHeader, EventTrace eventTrace, String rawPayload);
}
