package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;
import com.lilamaris.stockwolf.event.core.serializer.EventDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleEventRouter implements EventRouter {
    private static final Logger log = LoggerFactory.getLogger(SimpleEventRouter.class);
    private final EventListenerRegistrar eventListenerRegistrar;
    private final EventDeserializer eventDeserializer;

    public SimpleEventRouter(
            EventListenerRegistrar eventListenerRegistrar,
            EventDeserializer eventDeserializer
    ) {
        this.eventListenerRegistrar = eventListenerRegistrar;
        this.eventDeserializer = eventDeserializer;
    }

    @Override
    public void route(EventEnvelope eventEnvelope) {
        var eventKey = eventEnvelope.header().eventKey();

        var listener = eventListenerRegistrar.resolve(eventKey);

        if (listener == null) {
            log.warn("Inbound event batch claim was supply event envelope with event key {} but none of handler is support.", eventKey.name());
            return;
        }

        var payload = eventDeserializer.materialize(eventEnvelope.rawPayload(), listener.payload());

        invoke(
                listener,
                eventEnvelope.header(),
                eventEnvelope.trace(),
                payload
        );
    }

    private <P extends EventPayload> void invoke(
            EventListener<P> listener,
            EventHeader eventHeader,
            EventTrace eventTrace,
            EventPayload eventPayload
    ) {
        P payload = listener.payload().cast(eventPayload);
        listener.handle(eventHeader, eventTrace, payload);
    }
}
