package com.lilamaris.stockwolf.event.core.store;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventTrace;

public class SimpleStoredEventEnvelope implements StoredEventEnvelope {
    private final EventHeader header;
    private final EventTrace trace;
    private final String rawPayload;
    private final EventFlow eventFlow;
    private StoredEventStatus status;

    public SimpleStoredEventEnvelope(
            EventHeader header,
            EventTrace trace,
            String rawPayload,
            EventFlow eventFlow
    ) {
        this.header = header;
        this.trace = trace;
        this.rawPayload = rawPayload;
        this.status = StoredEventStatus.PROCESSING;
        this.eventFlow = eventFlow;
    }

    public static SimpleStoredEventEnvelope of(EventEnvelope eventEnvelope, EventFlow eventFlow) {
        return new SimpleStoredEventEnvelope(
                eventEnvelope.header(),
                eventEnvelope.trace(),
                eventEnvelope.rawPayload(),
                eventFlow
        );
    }

    @Override
    public StoredEventStatus status() {
        return status;
    }

    @Override
    public EventFlow eventFlow() {
        return eventFlow;
    }

    @Override
    public void markComplete() {
        status = StoredEventStatus.COMPLETE;
    }

    @Override
    public void markFail() {
        status = StoredEventStatus.FAIL;
    }

    @Override
    public EventHeader header() {
        return header;
    }

    @Override
    public EventTrace trace() {
        return trace;
    }

    @Override
    public String rawPayload() {
        return rawPayload;
    }
}
