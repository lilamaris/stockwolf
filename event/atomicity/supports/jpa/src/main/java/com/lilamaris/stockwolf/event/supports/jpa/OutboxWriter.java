package com.lilamaris.stockwolf.event.supports.jpa;

import com.lilamaris.stockwolf.event.core.payload.EventEnvelope;
import com.lilamaris.stockwolf.event.core.payload.EventPayload;
import tools.jackson.databind.ObjectMapper;

public class OutboxWriter {
    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxWriter(
            OutboxEventRepository repository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void enqueue(EventEnvelope<? extends EventPayload> event) {
        OutboxEventEntry e = new OutboxEventEntry();
        e.setEventId(event.trace().eventId());
        e.setEventKey(event.eventKey());
        e.setCorrelationId(event.trace().correlationId());
        e.setOccurredAt(event.occurredAt());
        e.setProducer(event.trace().producer());
        e.setAggregateType(event.trace().aggregateType());
        e.setAggregateId(event.trace().aggregateId());
        e.setPayloadJson(objectMapper.writeValueAsString(event.payload()));
        e.setStatus(OutboxEventStatus.PENDING);
        repository.save(e);
    }
}
