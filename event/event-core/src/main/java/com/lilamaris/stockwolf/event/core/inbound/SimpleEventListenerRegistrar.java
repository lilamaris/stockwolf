package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleEventListenerRegistrar implements EventListenerRegistrar {
    private final Map<String, EventListener<?>> registry;

    public SimpleEventListenerRegistrar(
            List<EventListener<?>> listeners
    ) {
        this.registry = listeners.stream().collect(Collectors.toMap(
                eventListener -> eventListener.key().name(),
                Function.identity()
        ));
    }

    @Override
    public void registerListener(EventListener<?> listener) {
        registry.put(listener.key().name(), listener);
    }

    @Override
    public <P extends EventPayload> @Nullable EventListener<P> resolve(EventKey key) {
        var listener = registry.get(key.name());
        if (listener == null) return null;

        @SuppressWarnings("unchecked")
        var casted = (EventListener<P>) listener;

        return casted;
    }
}
