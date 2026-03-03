package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventTrace;
import com.lilamaris.stockwolf.event.core.SimpleEventEnvelope;

public class SimpleEventEnvelopeFactory implements EventEnvelopeFactory {
    @Override
    public EventEnvelope build(EventHeader eventHeader, EventTrace eventTrace, String rawPayload) {
        return new SimpleEventEnvelope(
                eventHeader,
                eventTrace,
                rawPayload
        );
    }
}
