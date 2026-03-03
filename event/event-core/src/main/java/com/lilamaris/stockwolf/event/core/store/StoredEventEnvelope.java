package com.lilamaris.stockwolf.event.core.store;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

public interface StoredEventEnvelope extends EventEnvelope {
    StoredEventStatus status();

    EventFlow eventFlow();

    void markComplete();

    void markFail();
}
