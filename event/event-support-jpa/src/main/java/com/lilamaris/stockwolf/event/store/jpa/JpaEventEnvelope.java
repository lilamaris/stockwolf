package com.lilamaris.stockwolf.event.store.jpa;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventTrace;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Lob;

@Embeddable
public class JpaEventEnvelope implements EventEnvelope {
    @Embedded
    private JpaEventHeader eventHeader;

    @Embedded
    private JpaEventTrace eventTrace;

    @Lob
    @Column(name = "raw_payload", nullable = false)
    private String rawPayload;

    public JpaEventEnvelope(
            EventHeader eventHeader,
            EventTrace eventTrace,
            String rawPayload
    ) {
        this.eventHeader = JpaEventHeader.of(eventHeader);
        this.eventTrace = JpaEventTrace.of(eventTrace);
        this.rawPayload = rawPayload;
    }

    protected JpaEventEnvelope() {
    }

    public static JpaEventEnvelope of(EventEnvelope eventEnvelope) {
        return new JpaEventEnvelope(
                eventEnvelope.header(),
                eventEnvelope.trace(),
                eventEnvelope.rawPayload()
        );
    }

    @Override
    public EventHeader header() {
        return eventHeader;
    }

    @Override
    public EventTrace trace() {
        return eventTrace;
    }

    @Override
    public String rawPayload() {
        return rawPayload;
    }
}
