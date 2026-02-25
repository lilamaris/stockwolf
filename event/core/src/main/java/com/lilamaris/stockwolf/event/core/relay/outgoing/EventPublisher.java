package com.lilamaris.stockwolf.event.core.relay.outgoing;

public interface EventPublisher {
    void publish(String topic, String raw);
}

