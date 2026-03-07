package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

public interface EventRouter {
    void route(EventEnvelope eventEnvelope);
}
