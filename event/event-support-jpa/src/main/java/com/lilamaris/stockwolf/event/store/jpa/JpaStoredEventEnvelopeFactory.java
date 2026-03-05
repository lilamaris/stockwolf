package com.lilamaris.stockwolf.event.store.jpa;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.store.EventFlow;
import com.lilamaris.stockwolf.event.core.store.StoredEventEnvelope;
import com.lilamaris.stockwolf.event.core.store.StoredEventEnvelopeFactory;

public class JpaStoredEventEnvelopeFactory implements StoredEventEnvelopeFactory {
    @Override
    public StoredEventEnvelope build(EventEnvelope eventEnvelope, EventFlow eventFlow) {
        return JpaStoredEventEnvelope.of(eventEnvelope, eventFlow);
    }
}
