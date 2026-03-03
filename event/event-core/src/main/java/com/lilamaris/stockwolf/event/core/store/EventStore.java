package com.lilamaris.stockwolf.event.core.store;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

import java.util.List;

public interface EventStore {
    void accept(EventEnvelope eventEnvelope, EventFlow eventFlow);

    List<? extends StoredEventEnvelope> claimBatch(int size, EventFlow flow);

    void markComplete(String eventId);

    void markFailed(String eventId, String reason);
}
