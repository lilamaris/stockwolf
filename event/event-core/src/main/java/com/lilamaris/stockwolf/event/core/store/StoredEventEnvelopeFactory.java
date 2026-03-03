package com.lilamaris.stockwolf.event.core.store;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

public interface StoredEventEnvelopeFactory {
    StoredEventEnvelope build(EventEnvelope eventEnvelope, EventFlow eventFlow);
}
