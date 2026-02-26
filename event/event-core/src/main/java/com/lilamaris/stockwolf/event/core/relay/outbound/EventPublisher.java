package com.lilamaris.stockwolf.event.core.relay.outbound;

public interface EventPublisher {
    void publish(String topic, String raw);
}

