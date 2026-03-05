package com.lilamaris.stockwolf.event.store.jpa;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.SimpleEventKey;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import java.time.Instant;

@Embeddable
public class JpaEventHeader implements EventHeader {
    @Column(name = "event_key", nullable = false)
    private String eventKeyName;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Transient
    private EventKey eventKey;

    protected JpaEventHeader() {
    }

    public JpaEventHeader(
            EventKey eventKey,
            Instant occurredAt
    ) {
        this.eventKeyName = eventKey.name();
        this.occurredAt = occurredAt;
        this.eventKey = eventKey;
    }

    public static JpaEventHeader of(EventHeader eventHeader) {
        var h = new JpaEventHeader();

        h.eventKeyName = eventHeader.eventKey().name();
        h.occurredAt = eventHeader.occurredAt();
        h.eventKey = eventHeader.eventKey();
        return h;
    }

    @Override
    public EventKey eventKey() {
        if (eventKey == null) {
            eventKey = new SimpleEventKey(eventKeyName);
            return eventKey;
        }
        return eventKey;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}
