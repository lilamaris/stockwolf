package com.lilamaris.stockwolf.event.core.provider;

import org.jspecify.annotations.Nullable;

public interface CorrelationProvider {
    String getOrCreateCorrelationId();

    @Nullable String getCorrelationId();

    void setCorrelationId(@Nullable String correlationId);

    void clear();
}
