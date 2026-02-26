package com.lilamaris.stockwolf.event.supports.jpa;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.relay.outbound.OutboundStore;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public class OutboxStore implements OutboundStore {
    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxStore(
            OutboxEventRepository repository,
            ObjectMapper objectMapper,
            EventContextProvider contextProvider
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void enqueue(EventEnvelope<? extends EventPayload> event) {
        OutboxEventEntry e = new OutboxEventEntry();
        e.setHeader(event.header());
        e.setTrace(event.trace());
        e.setPayloadJson(objectMapper.writeValueAsString(event.payload()));
        e.setStatus(OutboxEventStatus.PENDING);
        repository.save(e);
    }

    @Override
    public List<EventEnvelope<?>> claimBatch(int size) {
        return List.of();
    }

    @Override
    public void enqueue(EventKey eventKey, EventPayload payload) {

    }

    @Override
    public void markSent(String eventId) {

    }

    @Override
    public void markFailed(String eventId, String reason) {

    }
}
