package com.lilamaris.stockwolf.event.support.jpa;

import com.lilamaris.stockwolf.event.core.EventTrace;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.jspecify.annotations.Nullable;

@Embeddable
public class JpaEventTrace implements EventTrace {
    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @Column(name = "causation_id")
    private String causationId;

    @Column(name = "producer", nullable = false)
    private String producer;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;

    public static JpaEventTrace of(EventTrace eventTrace) {
        var t = new JpaEventTrace();
        t.eventId = eventTrace.eventId();
        t.correlationId = eventTrace.correlationId();
        t.causationId = eventTrace.causationId();
        t.producer = eventTrace.producer();
        t.aggregateType = eventTrace.aggregateType();
        t.aggregateId = eventTrace.aggregateId();

        return t;
    }

    @Override
    public String eventId() {
        return eventId;
    }

    @Override
    public String correlationId() {
        return correlationId;
    }

    @Override
    public @Nullable String causationId() {
        return causationId;
    }

    @Override
    public String producer() {
        return producer;
    }

    @Override
    public String aggregateType() {
        return aggregateType;
    }

    @Override
    public String aggregateId() {
        return aggregateId;
    }
}
