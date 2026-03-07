package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.*;

public class SimpleEventEnvelopeFactory implements EventEnvelopeFactory {
    @Override
    public EventEnvelope build(EventHeader eventHeader, EventTrace eventTrace, String rawPayload) {
        var simpleEventHeader = SimpleEventHeader.of(eventHeader);
        var simpleEventTrace = SimpleEventTrace.of(eventTrace);
        return new SimpleEventEnvelope(
                simpleEventHeader,
                simpleEventTrace,
                rawPayload
        );
    }
}
