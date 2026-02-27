package com.lilamaris.stockwolf.event.supports.jpa;

import com.lilamaris.stockwolf.event.core.*;
import com.lilamaris.stockwolf.event.core.provider.CorrelationProvider;
import com.lilamaris.stockwolf.event.core.provider.EventIdProvider;
import com.lilamaris.stockwolf.event.core.provider.ProducerProvider;
import com.lilamaris.stockwolf.event.core.relay.outbound.OutboundStore;
import com.lilamaris.stockwolf.event.foundation.DefaultEventHeader;
import com.lilamaris.stockwolf.event.foundation.DefaultEventTrace;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

public class OutboxStore implements OutboundStore {
    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    private final Clock clock;
    private final EventIdProvider eventIdProvider;
    private final ProducerProvider producerProvider;
    private final CorrelationProvider correlationProvider;

    public OutboxStore(
            OutboxEventRepository repository,
            ObjectMapper objectMapper,
            Clock clock,
            EventIdProvider eventIdProvider,
            ProducerProvider producerProvider,
            CorrelationProvider correlationProvider
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.clock = clock;
        this.eventIdProvider = eventIdProvider;
        this.producerProvider = producerProvider;
        this.correlationProvider = correlationProvider;
    }

    @Override
    public List<EventEnvelope<?>> claimBatch(int size) {
        return List.of();
    }

    @Override
    public void enqueue(EventKey eventKey, EventContext context, EventPayload payload) {
        String eventId = eventIdProvider.newId();
        String producer = producerProvider.producer();
        var occurredAt = clock.instant();

        String correlationId = Optional.ofNullable(context.correlationId())
                .filter(s -> !s.isBlank())
                .orElse(correlationProvider.getOrCreateCorrelationId());

        String causationId = Optional.ofNullable(context.causationId())
                .filter(s -> !s.isBlank())
                .orElse(null);

        EventHeader h = new DefaultEventHeader(
                eventKey,
                occurredAt
        );

        EventTrace t = new DefaultEventTrace(
                eventId,
                correlationId,
                causationId,
                producer,
                context.aggregateType(),
                context.aggregateId()
        );

        OutboxEventEntry e = new OutboxEventEntry();
        e.setHeader(h);
        e.setTrace(t);
        e.setPayloadJson(objectMapper.writeValueAsString(payload));
        e.setStatus(OutboxEventStatus.PENDING);

        repository.save(e);
    }

    @Override
    public void markSent(String eventId) {
        var e = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Event with id %s not exists.", eventId
                )));

        e.setStatus(OutboxEventStatus.SENT);
    }

    @Override
    public void markFailed(String eventId, String reason) {
        var e = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Event with id %s not exists.", eventId
                )));

        e.setStatus(OutboxEventStatus.FAILED);
    }
}
