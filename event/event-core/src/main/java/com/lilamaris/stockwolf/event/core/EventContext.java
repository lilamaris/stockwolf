package com.lilamaris.stockwolf.event.core;

import org.jspecify.annotations.Nullable;

public interface EventContext {
    String aggregateType();

    String aggregateId();

    @Nullable String correlationId();

    @Nullable String causationId();
}
