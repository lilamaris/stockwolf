package com.lilamaris.stockwolf.event.core.relay.outbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventPayload;

import java.util.List;

public interface OutboundStore {
    List<EventEnvelope<?>> claimBatch(int size);

    void enqueue(EventKey eventKey, EventContext context, EventPayload payload);

    void markSent(String eventId);

    void markFailed(String eventId, String reason);
}
