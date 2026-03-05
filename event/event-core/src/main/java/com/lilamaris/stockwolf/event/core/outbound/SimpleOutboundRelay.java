package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.store.EventFlow;
import com.lilamaris.stockwolf.event.core.store.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleOutboundRelay implements OutboundRelay {
    private static final Logger log = LoggerFactory.getLogger(SimpleOutboundRelay.class);
    private final EventStore store;
    private final EventSender eventSender;

    public SimpleOutboundRelay(
            EventStore store,
            EventSender eventSender
    ) {
        this.store = store;
        this.eventSender = eventSender;
    }

    public void batch(int size) {
        var batch = store.claimBatch(size, EventFlow.OUTBOUND);

        log.debug("Outbound batch claim {} events.", batch.size());

        for (var e : batch) {
            try {
                String topic = e.trace().producer() + ".events";
                EventKey eventKey = e.header().eventKey();
                eventSender.send(topic, eventKey, e.rawPayload());
                store.markComplete(e.trace().eventId());
            } catch (Exception ex) {
                store.markFailed(e.trace().eventId(), ex.getMessage());
            }
        }
    }
}
