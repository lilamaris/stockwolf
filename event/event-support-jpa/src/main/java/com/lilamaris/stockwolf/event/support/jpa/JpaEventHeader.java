package com.lilamaris.stockwolf.event.support.jpa;

import com.lilamaris.stockwolf.event.core.DefaultEventKey;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventKey;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Instant;

@Embeddable
public class JpaEventHeader implements EventHeader {
    @Column(name = "event_key", nullable = false)
    private String eventKeyName;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    public static JpaEventHeader of(EventHeader eventHeader) {
        var h = new JpaEventHeader();

        h.eventKeyName = eventHeader.eventKey().name();
        h.occurredAt = eventHeader.occurredAt();

        return h;
    }

    @Override
    public EventKey eventKey() {
        return DefaultEventKey.of(eventKeyName);
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}
