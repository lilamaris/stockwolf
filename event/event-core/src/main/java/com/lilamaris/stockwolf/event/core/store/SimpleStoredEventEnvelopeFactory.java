package com.lilamaris.stockwolf.event.core.store;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

public class SimpleStoredEventEnvelopeFactory implements StoredEventEnvelopeFactory {
    @Override
    public StoredEventEnvelope build(EventEnvelope eventEnvelope, EventFlow eventFlow) {
        return SimpleStoredEventEnvelope.of(eventEnvelope, eventFlow);
    }
}
