package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

import java.util.List;

public interface InboundStore {
    void enqueue(String raw);

    List<? extends EventEnvelope> claimBatch(int size);

    void markComplete(String eventId);

    void markFailed(String eventId, String reason);
}
