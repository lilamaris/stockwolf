package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.relay.outgoing.EventHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventRegistry {
    private final Map<String, EventDefinition<?>> definitionRegistry;
    private final Map<String, EventHandler<?>> handlerRegistry;

    public EventRegistry(
            List<EventDefinition<?>> definitions,
            List<EventHandler<?>> handlers
    ) {
        this.definitionRegistry = definitions.stream().collect(Collectors.toUnmodifiableMap(
                EventDefinition::key,
                Function.identity()
        ));

        this.handlerRegistry = handlers.stream().collect(Collectors.toUnmodifiableMap(
                EventHandler::key,
                Function.identity()
        ));
    }

    public Optional<EventDefinition<?>> resolveDefinition(String eventKey) {
        return Optional.ofNullable(definitionRegistry.get(eventKey));
    }

    public Optional<EventHandler<?>> resolveHandler(String eventKey) {
        return Optional.ofNullable(handlerRegistry.get(eventKey));
    }
}
