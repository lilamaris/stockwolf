package com.lilamaris.stockwolf.event.support.jpa;

import com.lilamaris.stockwolf.event.core.*;
import com.lilamaris.stockwolf.event.core.provider.CorrelationProvider;
import com.lilamaris.stockwolf.event.core.provider.EventIdProvider;
import com.lilamaris.stockwolf.event.core.provider.ProducerProvider;
import com.lilamaris.stockwolf.event.core.relay.outbound.OutboundStore;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.foundation.DefaultEventHeader;
import com.lilamaris.stockwolf.event.foundation.DefaultEventTrace;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

public class OutboxStore implements OutboundStore {
    private final OutboxEventRepository repository;
    private final EventCodec eventCodec;

    private final Clock clock;
    private final EventIdProvider eventIdProvider;
    private final ProducerProvider producerProvider;
    private final CorrelationProvider correlationProvider;

    public OutboxStore(
            OutboxEventRepository repository,
            EventCodec eventCodec,
            Clock clock,
            EventIdProvider eventIdProvider,
            ProducerProvider producerProvider,
            CorrelationProvider correlationProvider
    ) {
        this.repository = repository;
        this.eventCodec = eventCodec;
        this.clock = clock;
        this.eventIdProvider = eventIdProvider;
        this.producerProvider = producerProvider;
        this.correlationProvider = correlationProvider;
    }

    @Override
    @Transactional
    public List<? extends EventEnvelope> claimBatch(int size) {
        var entries = repository.findPending(PageRequest.of(0, size));

        for (var e : entries) {
            e.setStatus(OutboxEventStatus.PROCESSING);
            e.setAttemptCount(e.getAttemptCount() + 1);
        }

        repository.flush();

        return entries;
    }

    @Override
    public <P extends EventPayload> void enqueue(EventKey eventKey, EventContext context, P payload) {
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

        String raw = eventCodec.encodePayload(payload);

        var e = OutboxEventEnvelope.of(eventId, h, t, raw);

        repository.save(e);
    }

    @Override
    @Transactional
    public void markSent(String eventId) {
        var e = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Event with id %s not exists.", eventId
                )));

        e.setStatus(OutboxEventStatus.SENT);
        e.setSentAt(clock.instant());
    }

    @Override
    @Transactional
    public void markFailed(String eventId, String reason) {
        var e = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Event with id %s not exists.", eventId
                )));

        e.setStatus(OutboxEventStatus.FAILED);
    }
}
