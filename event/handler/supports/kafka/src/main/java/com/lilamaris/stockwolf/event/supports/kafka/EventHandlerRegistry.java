package com.lilamaris.stockwolf.event.supports.kafka;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventHandlerRegistry {
    private final Map<String, EventHandler<?>> registry;

    public EventHandlerRegistry(
            List<EventHandler<?>> handlers
    ) {
        this.registry = handlers.stream().collect(Collectors.toUnmodifiableMap(
                EventHandler::key,
                Function.identity()
        ));
    }

    public Optional<EventHandler<?>> resolve(String eventKey) {
        return Optional.ofNullable(registry.get(eventKey));
    }
}
