package com.lilamaris.stockwolf.event.supports.jpa;

import com.lilamaris.stockwolf.event.core.payload.EventHeader;
import com.lilamaris.stockwolf.event.core.payload.EventTrace;
import com.lilamaris.stockwolf.event.foundation.payload.DefaultEventHeader;
import com.lilamaris.stockwolf.event.foundation.payload.DefaultEventTrace;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "outbox_event",
        indexes = {
                @Index(name = "idx_outbox_status_occurred", columnList = "status, occurredAt")
        }
)
public class OutboxEventEntry {
    @Id
    private String eventId;

    @Column(nullable = false)
    private String eventKey;

    @Column(nullable = false)
    private String correlationId;

    @Column(nullable = false)
    private String causationId;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false)
    private String producer;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String aggregateId;

    @Lob
    @Column(nullable = false)
    private String payloadJson;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxEventStatus status;
    private Instant sentAt;
    private int attemptCount;

    public EventHeader getHeader() {
        return new DefaultEventHeader(eventKey, occurredAt);
    }

    public void setHeader(EventHeader header) {
        this.eventKey = header.eventKey();
        this.occurredAt = header.occurredAt();
    }

    public EventTrace getTrace() {
        return new DefaultEventTrace(
                eventId,
                correlationId,
                causationId,
                producer,
                aggregateType,
                aggregateId
        );
    }

    public void setTrace(EventTrace trace) {
        this.eventId = trace.eventId();
        this.producer = trace.producer();
        this.aggregateId = trace.aggregateId();
        this.aggregateType = trace.aggregateType();
        this.causationId = trace.causationId();
        this.correlationId = trace.correlationId();
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String raw) {
        this.payloadJson = raw;
    }

    public OutboxEventStatus getStatus() {
        return status;
    }

    public void setStatus(OutboxEventStatus status) {
        this.status = status;
    }

}
