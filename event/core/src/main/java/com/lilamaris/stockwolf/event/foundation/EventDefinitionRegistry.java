package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.EventPayload;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventDefinitionRegistry {
    private final Map<String, Class<? extends EventPayload>> registry;

    public EventDefinitionRegistry(List<EventDefinition<?>> definitions) {
        this.registry = definitions.stream().collect(Collectors.toUnmodifiableMap(
                EventDefinition::key,
                EventDefinition::payload
        ));
    }

    public Optional<Class<? extends EventPayload>> resolve(String eventKey) {
        return Optional.ofNullable(registry.get(eventKey));
    }
}
