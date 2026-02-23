package com.lilamaris.stockwolf.event.core;

public interface EventPublisher {
    void publish(EventEnvelope event);
}
