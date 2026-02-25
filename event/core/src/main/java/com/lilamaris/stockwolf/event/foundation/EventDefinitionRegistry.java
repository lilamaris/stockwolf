package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventDefinition;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventDefinitionRegistry {
    private final Map<String, EventDefinition<?>> registry;

    public EventDefinitionRegistry(List<EventDefinition<?>> definitions) {
        this.registry = definitions.stream().collect(Collectors.toUnmodifiableMap(
                EventDefinition::key,
                Function.identity()
        ));
    }

    public Optional<EventDefinition<?>> resolve(String eventKey) {
        return Optional.ofNullable(registry.get(eventKey));
    }
}
