package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.EventEnvelope;

public interface TopicFactory {
    String build(EventEnvelope eventEnvelope);
}
