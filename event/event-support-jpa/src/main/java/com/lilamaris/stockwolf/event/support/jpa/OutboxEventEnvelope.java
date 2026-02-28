package com.lilamaris.stockwolf.event.support.jpa;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventTrace;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "outbox_event_envelope",
        indexes = {
                @Index(name = "idx_outbox_status_occurred", columnList = "status, occurred_at")
        }
)
public class OutboxEventEnvelope implements EventEnvelope {
    @Id
    private String id;

    @Embedded
    private JpaEventHeader eventHeader;

    @Embedded
    private JpaEventTrace eventTrace;

    @Lob
    private String rawPayload;

    @Enumerated(EnumType.STRING)
    private OutboxEventStatus status;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "sent_at")
    private Instant sentAt;

    public static OutboxEventEnvelope of(
            String id,
            EventHeader eventHeader,
            EventTrace eventTrace,
            String rawPayload
    ) {
        var e = new OutboxEventEnvelope();

        e.id = id;
        e.eventHeader = JpaEventHeader.of(eventHeader);
        e.eventTrace = JpaEventTrace.of(eventTrace);
        e.rawPayload = rawPayload;
        e.status = OutboxEventStatus.PENDING;
        e.attemptCount = 0;

        return e;
    }

    @Override
    public EventHeader header() {
        return eventHeader;
    }

    @Override
    public String raw() {
        return rawPayload;
    }

    @Override
    public EventTrace trace() {
        return eventTrace;
    }

    public OutboxEventStatus getStatus() {
        return status;
    }

    public void setStatus(OutboxEventStatus status) {
        this.status = status;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }
}
