package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

public interface EventSender {
    void send(EventEnvelope eventEnvelope);
}

