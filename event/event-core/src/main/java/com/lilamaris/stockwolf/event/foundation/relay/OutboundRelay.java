package com.lilamaris.stockwolf.event.foundation.relay;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.relay.outbound.EventPublisher;
import com.lilamaris.stockwolf.event.core.relay.outbound.OutboundStore;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;

import java.util.List;

public class OutboundRelay {
    private final OutboundStore store;
    private final EventPublisher publisher;
    private final EventCodec codec;

    public OutboundRelay(
            OutboundStore store,
            EventPublisher publisher,
            EventCodec codec
    ) {
        this.store = store;
        this.publisher = publisher;
        this.codec = codec;
    }

    public void batch(int size) {
        List<EventEnvelope<?>> batch = store.claimBatch(size);

        for (var e : batch) {
            try {
                String topic = e.trace().producer() + ".events";
                String raw = codec.encode(e);
                publisher.publish(topic, raw);
                store.markSent(e.trace().eventId());
            } catch (Exception ex) {
                store.markFailed(e.trace().eventId(), ex.getMessage());
            }
        }
    }
}
