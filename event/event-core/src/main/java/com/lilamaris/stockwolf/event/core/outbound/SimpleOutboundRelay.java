package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.factory.EventEnvelopeFactory;
import com.lilamaris.stockwolf.event.core.store.EventFlow;
import com.lilamaris.stockwolf.event.core.store.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleOutboundRelay implements OutboundRelay {
    private static final Logger log = LoggerFactory.getLogger(SimpleOutboundRelay.class);
    private final EventStore store;
    private final EventSender eventSender;
    private final EventEnvelopeFactory eventEnvelopeFactory;

    public SimpleOutboundRelay(
            EventStore store,
            EventSender eventSender,
            EventEnvelopeFactory eventEnvelopeFactory
    ) {
        this.store = store;
        this.eventSender = eventSender;
        this.eventEnvelopeFactory = eventEnvelopeFactory;
    }

    public void batch(int size) {
        var batch = store.claimBatch(size, EventFlow.OUTBOUND);

        for (var e : batch) {
            try {
                eventSender.send(eventEnvelopeFactory.build(e.header(), e.trace(), e.rawPayload()));
                store.markComplete(e.trace().eventId());
            } catch (Exception ex) {
                store.markFailed(e.trace().eventId(), ex.getMessage());
            }
        }
    }
}
