package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

public class ProducerBasedTopicFactory implements TopicFactory {
    private final String topic;

    public ProducerBasedTopicFactory(String topic) {
        this.topic = topic;
    }

    @Override
    public String build(EventEnvelope eventEnvelope) {
        return String.join(".", eventEnvelope.trace().producer(), topic);
    }
}
