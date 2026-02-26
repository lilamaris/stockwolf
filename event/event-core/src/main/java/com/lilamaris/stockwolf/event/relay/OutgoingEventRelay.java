package com.lilamaris.stockwolf.event.relay;

import com.lilamaris.stockwolf.event.core.payload.EventEnvelope;
import com.lilamaris.stockwolf.event.core.relay.outgoing.EventPublisher;
import com.lilamaris.stockwolf.event.core.relay.outgoing.OutgoingStore;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;

import java.util.List;

public class OutgoingEventRelay {
    private final OutgoingStore store;
    private final EventPublisher publisher;
    private final EventCodec codec;

    public OutgoingEventRelay(
            OutgoingStore store,
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
