package com.lilamaris.stockwolf.event.core.store;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleStoredEventEnvelopeFactory implements StoredEventEnvelopeFactory {
    private static final Logger log = LoggerFactory.getLogger(SimpleStoredEventEnvelopeFactory.class);

    @Override
    public StoredEventEnvelope build(EventEnvelope eventEnvelope, EventFlow eventFlow) {
        log.debug("""
                Build event envelope
                trace={}""", eventEnvelope.trace());
        return SimpleStoredEventEnvelope.of(eventEnvelope, eventFlow);
    }
}
