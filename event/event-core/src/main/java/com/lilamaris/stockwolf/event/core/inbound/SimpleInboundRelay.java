package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.store.EventFlow;
import com.lilamaris.stockwolf.event.core.store.EventStore;

import java.util.List;

public class SimpleInboundRelay implements InboundRelay {
    private final EventStore eventStore;
    private final EventRouter eventRouter;

    public SimpleInboundRelay(
            EventStore eventStore,
            EventRouter eventRouter
    ) {
        this.eventStore = eventStore;
        this.eventRouter = eventRouter;
    }

    @Override
    public void batch(int size) {
        List<? extends EventEnvelope> entries = eventStore.claimBatch(size, EventFlow.INBOUND);

        for (var e : entries) {
            eventRouter.route(e);
        }
    }
}
