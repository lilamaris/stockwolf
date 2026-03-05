package com.lilamaris.stockwolf.event.store.jpa;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventTrace;
import com.lilamaris.stockwolf.event.core.store.EventFlow;
import com.lilamaris.stockwolf.event.core.store.StoredEventEnvelope;
import com.lilamaris.stockwolf.event.core.store.StoredEventStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "stored_event")
public class JpaStoredEventEnvelope implements StoredEventEnvelope {
    @Id
    private String id;

    @Embedded
    private JpaEventHeader eventHeader;

    @Embedded
    private JpaEventTrace eventTrace;

    @Lob
    @Column(name = "raw_payload", nullable = false)
    private String rawPayload;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_flow", nullable = false)
    private EventFlow eventFlow;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StoredEventStatus status;

    protected JpaStoredEventEnvelope() {
    }

    public JpaStoredEventEnvelope(
            String id,
            JpaEventHeader jpaEventHeader,
            JpaEventTrace jpaEventTrace,
            String rawPayload,
            EventFlow eventFlow
    ) {
        this.id = id;
        this.eventHeader = jpaEventHeader;
        this.eventTrace = jpaEventTrace;
        this.rawPayload = rawPayload;
        this.eventFlow = eventFlow;
        this.status = StoredEventStatus.PROCESSING;
    }

    public static JpaStoredEventEnvelope of(
            EventEnvelope eventEnvelope,
            EventFlow eventFlow
    ) {
        var id = eventEnvelope.trace().eventId();
        var jpaEventHeader = JpaEventHeader.of(eventEnvelope.header());
        var jpaEventTrace = JpaEventTrace.of(eventEnvelope.trace());

        return new JpaStoredEventEnvelope(
                id,
                jpaEventHeader,
                jpaEventTrace,
                eventEnvelope.rawPayload(),
                eventFlow
        );
    }

    @Override
    public EventFlow eventFlow() {
        return eventFlow;
    }

    @Override
    public StoredEventStatus status() {
        return status;
    }

    @Override
    public void markComplete() {
        this.status = StoredEventStatus.COMPLETE;
    }

    @Override
    public void markFail() {
        this.status = StoredEventStatus.FAIL;
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
