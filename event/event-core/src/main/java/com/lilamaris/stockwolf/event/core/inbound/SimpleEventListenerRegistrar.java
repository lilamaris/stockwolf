package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleEventListenerRegistrar implements EventListenerRegistrar {
    private static final Logger log = LoggerFactory.getLogger(SimpleEventListenerRegistrar.class);
    private final Map<String, EventListener<?>> registry;

    public SimpleEventListenerRegistrar(
            List<EventListener<?>> listeners
    ) {
        this.registry = listeners.stream().collect(Collectors.toMap(
                eventListener -> eventListener.key().name(),
                Function.identity()
        ));

        log.debug("""
                EventListenerRegistrar Initialized with {} listeners""", listeners.size());
    }

    @Override
    public void registerListener(EventListener<?> listener) {
        EventKey key = listener.key();
        if (registry.containsKey(key.name())) {
            log.warn("""
                    Event listener registrar received register request for already existing key={}.
                    Ignore registration for this key""", key.name());
        }
        registry.put(key.name(), listener);
    }

    @Override
    public <P extends EventPayload> @Nullable EventListener<P> resolve(EventKey key) {
        var listener = registry.get(key.name());
        if (listener == null) return null;

        @SuppressWarnings("unchecked")
        var casted = (EventListener<P>) listener;

        return casted;
    }

    @Override
    public List<EventListener<?>> listAll() {
        return registry.values().stream().toList();
    }
}
