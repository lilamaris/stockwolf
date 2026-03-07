package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogOnlyEventRouter implements EventRouter {
    private static final Logger log = LoggerFactory.getLogger(LogOnlyEventRouter.class);

    @Override
    public void route(EventEnvelope eventEnvelope) {
        var eventId = eventEnvelope.trace().eventId();
        var eventKey = eventEnvelope.header().eventKey();
        var rawPayload = eventEnvelope.rawPayload();

        log.debug("""
                Event routing request received.
                id={}
                key={}
                payload={}""", eventId, eventKey.name(), rawPayload);
    }
}
