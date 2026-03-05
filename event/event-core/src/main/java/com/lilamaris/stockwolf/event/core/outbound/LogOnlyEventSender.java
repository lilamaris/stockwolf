package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogOnlyEventSender implements EventSender {
    private static final Logger log = LoggerFactory.getLogger(LogOnlyEventSender.class);

    @Override
    public void send(EventEnvelope eventEnvelope) {
        var eventId = eventEnvelope.trace().eventId();
        var eventKey = eventEnvelope.header().eventKey();
        var rawPayload = eventEnvelope.rawPayload();

        log.debug("""
                Send requested message
                eventId={}
                eventKey={}
                rawPayload={}""", eventId, eventKey, rawPayload);
    }
}
