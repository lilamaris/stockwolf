package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SimpleInboundRelay implements InboundRelay {
    private static final Logger log = LoggerFactory.getLogger(SimpleInboundRelay.class);
    private final EventListenerRegistrar eventListenerRegistrar;
    private final EventCodec eventCodec;
    private final InboundStore inboundStore;

    public SimpleInboundRelay(
            EventListenerRegistrar eventListenerRegistrar,
            EventCodec eventCodec,
            InboundStore inboundStore
    ) {
        this.eventListenerRegistrar = eventListenerRegistrar;
        this.eventCodec = eventCodec;
        this.inboundStore = inboundStore;
    }

    @Override
    public void batch(int size) {
        List<? extends EventEnvelope> entries = inboundStore.claimBatch(size);

        for (var e : entries) {
            var eventHeader = e.header();
            var eventkey = eventHeader.eventKey();

            var listener = eventListenerRegistrar.resolve(eventkey);
            if (listener == null) {
                log.warn("Inbound event batch claim was supply event envelope with event key {} but none of handler is support.", eventkey.name());
                continue;
            }

            var payload = eventCodec.decodePayload(e.raw(), listener.payload());

            invoke(
                    listener,
                    e.header(),
                    e.trace(),
                    payload
            );
        }

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
