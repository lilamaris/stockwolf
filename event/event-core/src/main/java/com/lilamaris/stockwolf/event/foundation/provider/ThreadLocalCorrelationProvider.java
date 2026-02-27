package com.lilamaris.stockwolf.event.foundation.provider;

import com.lilamaris.stockwolf.event.core.provider.CorrelationProvider;
import org.jspecify.annotations.Nullable;
import org.slf4j.MDC;

import java.util.UUID;

public class ThreadLocalCorrelationProvider implements CorrelationProvider {
    private static final ThreadLocal<String> t = new ThreadLocal<>();
    private static final String MDC_KEY = "correlationId";

    @Override
    public String getOrCreateCorrelationId() {
        String existing = t.get();
        if (existing != null && !existing.isBlank()) return existing;

        String created = UUID.randomUUID().toString();
        setCorrelationId(created);
        return created;
    }

    @Override
    public @Nullable String getCorrelationId() {
        String v = t.get();
        if (v == null || v.isBlank()) return null;
        return v;
    }

    @Override
    public void setCorrelationId(@Nullable String correlationId) {
        if (correlationId == null || correlationId.isBlank()) {
            clear();
            return;
        }
        t.set(correlationId);
        MDC.put(MDC_KEY, correlationId);
    }

    @Override
    public void clear() {
        t.remove();
        MDC.remove(MDC_KEY);
    }
}
