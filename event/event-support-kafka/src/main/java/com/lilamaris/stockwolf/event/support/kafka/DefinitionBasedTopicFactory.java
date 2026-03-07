package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.outbound.EventDefinition;

public class DefinitionBasedTopicFactory implements TopicFactory {
    private final String topic;

    public DefinitionBasedTopicFactory(String topic) {
        this.topic = topic;
    }

    @Override
    public String build(EventDefinition<?> eventDefinition) {
        return String.join(".", eventDefinition.producer(), topic);
    }
}
