package com.lilamaris.stockwolf.event.core.relay.outgoing;

import com.lilamaris.stockwolf.event.core.payload.EventEnvelope;

import java.util.List;

public interface OutgoingStore {
    List<EventEnvelope<?>> claimBatch(int size);

    void markSent(String eventId);

    void markFailed(String eventId, String reason);
}
