package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.outbound.EventDefinition;

public interface TopicFactory {
    String build(EventDefinition<?> eventDefinition);
}
