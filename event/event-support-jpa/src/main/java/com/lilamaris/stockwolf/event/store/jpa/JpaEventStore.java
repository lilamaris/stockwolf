package com.lilamaris.stockwolf.event.store.jpa;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.store.EventFlow;
import com.lilamaris.stockwolf.event.core.store.EventStore;
import com.lilamaris.stockwolf.event.core.store.StoredEventEnvelope;
import com.lilamaris.stockwolf.event.core.store.StoredEventStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class JpaEventStore implements EventStore {
    private static final Logger log = LoggerFactory.getLogger(JpaEventStore.class);
    private final JpaStoredEventEnvelopeRepository repository;

    public JpaEventStore(
            JpaStoredEventEnvelopeRepository jpaStoredEventEnvelopeRepository
    ) {
        this.repository = jpaStoredEventEnvelopeRepository;
    }

    @Override
    @Transactional
    public void accept(EventEnvelope eventEnvelope, EventFlow eventFlow) {
        var storedEventEnvelope = JpaStoredEventEnvelope.of(eventEnvelope, eventFlow);
        repository.save(storedEventEnvelope);

        log.debug("""
                Stored EventEnvelope
                EventId={}
                EventFlow={}""", storedEventEnvelope.trace().eventId(), eventFlow);
    }

    @Override
    @Transactional
    public List<? extends StoredEventEnvelope> claimBatch(int size, EventFlow flow) {
        var page = PageRequest.of(0, size);
        return repository.claimBatch(StoredEventStatus.PROCESSING, flow, page);
    }

    @Override
    @Transactional
    public void markComplete(String eventId) {
        var event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Event not exists with eventId '%s'", eventId
                )));

        event.markComplete();
    }

    @Override
    @Transactional
    public void markFailed(String eventId, String reason) {
        var event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Event not exists with eventId '%s'", eventId
                )));

        event.markFail();
    }
}
