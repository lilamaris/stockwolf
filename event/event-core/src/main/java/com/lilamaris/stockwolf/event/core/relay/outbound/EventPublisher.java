package com.lilamaris.stockwolf.event.core.relay.outbound;

import com.lilamaris.stockwolf.event.core.EventKey;

public interface EventPublisher {
    void publish(String topic, EventKey eventKey, String raw);
}

