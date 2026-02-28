package com.lilamaris.stockwolf.event.core.relay.outbound;

import com.lilamaris.stockwolf.event.core.EventContext;
import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;

import java.util.List;

public interface OutboundStore {
    List<? extends EventEnvelope> claimBatch(int size);

    <P extends EventPayload> void enqueue(EventKey eventKey, EventContext context, P payload);

    void markSent(String eventId);

    void markFailed(String eventId, String reason);
}
