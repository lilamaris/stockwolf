package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.EventTrace;
import com.lilamaris.stockwolf.event.core.SimpleEventTrace;
import com.lilamaris.stockwolf.event.core.provider.CorrelationProvider;
import com.lilamaris.stockwolf.event.core.provider.EventIdProvider;
import com.lilamaris.stockwolf.event.core.provider.ProducerProvider;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class SimpleEventTraceFactory implements EventTraceFactory {
    private final EventIdProvider eventIdProvider;
    private final ProducerProvider producerProvider;
    private final CorrelationProvider correlationProvider;

    public SimpleEventTraceFactory(
            EventIdProvider eventIdProvider,
            ProducerProvider producerProvider,
            CorrelationProvider correlationProvider
    ) {
        this.eventIdProvider = eventIdProvider;
        this.producerProvider = producerProvider;
        this.correlationProvider = correlationProvider;
    }

    @Override
    public EventTrace build(String aggregateType, String aggregateId, @Nullable String correlationId, @Nullable String causationId) {
        String finalCorrelationId = Optional.ofNullable(correlationId).orElseGet(correlationProvider::getOrCreateCorrelationId);
        String producer = producerProvider.producer();

        return new SimpleEventTrace(
                eventIdProvider.newId(),
                finalCorrelationId,
                causationId,
                producer,
                aggregateType,
                aggregateId
        );
    }
}
