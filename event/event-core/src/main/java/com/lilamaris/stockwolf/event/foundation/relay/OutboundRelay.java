package com.lilamaris.stockwolf.event.foundation.relay;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.relay.outbound.EventPublisher;
import com.lilamaris.stockwolf.event.core.relay.outbound.OutboundStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutboundRelay {
    private static final Logger log = LoggerFactory.getLogger(OutboundRelay.class);
    private final OutboundStore store;
    private final EventPublisher publisher;

    public OutboundRelay(
            OutboundStore store,
            EventPublisher publisher
    ) {
        this.store = store;
        this.publisher = publisher;
    }

    public void batch(int size) {
        var batch = store.claimBatch(size);

        for (var e : batch) {
            try {
                String topic = e.trace().producer() + ".events";
                EventKey eventKey = e.header().eventKey();
                publisher.publish(topic, eventKey, e.raw());
                store.markSent(e.trace().eventId());
            } catch (Exception ex) {
                store.markFailed(e.trace().eventId(), ex.getMessage());
            }
        }
    }
}
