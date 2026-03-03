package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.*;
import com.lilamaris.stockwolf.event.core.factory.EventEnvelopeFactory;
import com.lilamaris.stockwolf.event.core.factory.EventHeaderFactory;
import com.lilamaris.stockwolf.event.core.factory.EventTraceFactory;
import com.lilamaris.stockwolf.event.core.serializer.PayloadSerializer;
import com.lilamaris.stockwolf.event.core.store.EventFlow;
import com.lilamaris.stockwolf.event.core.store.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleEventPublisher implements EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(SimpleEventPublisher.class);
    private final EventHeaderFactory eventHeaderFactory;
    private final EventTraceFactory eventTraceFactory;
    private final EventEnvelopeFactory eventEnvelopeFactory;
    private final EventStore eventStore;
    private final PayloadSerializer payloadSerializer;

    public SimpleEventPublisher(
            EventHeaderFactory eventHeaderFactory,
            EventTraceFactory eventTraceFactory,
            EventEnvelopeFactory eventEnvelopeFactory,
            EventStore eventStore,
            PayloadSerializer payloadSerializer
    ) {
        this.eventHeaderFactory = eventHeaderFactory;
        this.eventTraceFactory = eventTraceFactory;
        this.eventEnvelopeFactory = eventEnvelopeFactory;
        this.eventStore = eventStore;
        this.payloadSerializer = payloadSerializer;
    }

    @Override
    public <P extends EventPayload> void publish(
            EventKey eventKey,
            EventDynamicContext eventDynamicContext,
            P eventPayload
    ) {
        EventHeader eventHeader = eventHeaderFactory.build(eventKey);
        EventTrace eventTrace = eventTraceFactory.build(
                eventDynamicContext.aggregateType(),
                eventDynamicContext.aggregateId(),
                eventDynamicContext.correlationId(),
                eventDynamicContext.causationId()
        );

        String rawPayload = payloadSerializer.stringify(eventPayload);

        EventEnvelope eventEnvelope = eventEnvelopeFactory.build(eventHeader, eventTrace, rawPayload);

        log.info("""
                Publish:
                EventHeader={}
                EventDynamicContext={},
                Payload(Raw)={}
                """, eventHeader, eventDynamicContext, rawPayload);

        eventStore.accept(eventEnvelope, EventFlow.OUTBOUND);
    }
}
