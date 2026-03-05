package com.lilamaris.stockwolf.event.core.store;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleEventStore implements EventStore {
    private static final Logger log = LoggerFactory.getLogger(SimpleEventStore.class);
    private final Map<String, StoredEventEnvelope> entryMap;
    private final StoredEventEnvelopeFactory storedEventEnvelopeFactory;

    public SimpleEventStore(
            StoredEventEnvelopeFactory storedEventEnvelopeFactory
    ) {
        this.entryMap = new HashMap<>();
        this.storedEventEnvelopeFactory = storedEventEnvelopeFactory;
    }

    @Override
    public void accept(EventEnvelope eventEnvelope, EventFlow eventFlow) {
        StoredEventEnvelope entry = storedEventEnvelopeFactory.build(eventEnvelope, eventFlow);

        String eventId = eventEnvelope.trace().eventId();

        entryMap.putIfAbsent(eventId, entry);

        log.debug("""
                PutIfAbsent({}, {})
                Current={}
                """, eventId, entry, entryMap);
    }

    @Override
    public List<? extends StoredEventEnvelope> claimBatch(int size, EventFlow flow) {
        var result = entryMap.values().stream()
                .filter(e -> e.status().equals(StoredEventStatus.PROCESSING))
                .filter(e -> e.eventFlow().equals(flow))
                .sorted(Comparator.comparing(e -> e.header().occurredAt()))
                .limit(size)
                .toList();

        log.debug("""
                Batched={}
                """, result);

        return result;
    }

    @Override
    public void markComplete(String eventId) {
        if (!entryMap.containsKey(eventId)) return;

        var entry = entryMap.get(eventId);
        entry.markComplete();

        log.debug("""
                Mark completed={}
                """, eventId);
    }

    @Override
    public void markFailed(String eventId, String reason) {
        if (!entryMap.containsKey(eventId)) return;

        var entry = entryMap.get(eventId);
        entry.markFail();

        log.debug("""
                Mark failed={}
                """, eventId);
    }
}
