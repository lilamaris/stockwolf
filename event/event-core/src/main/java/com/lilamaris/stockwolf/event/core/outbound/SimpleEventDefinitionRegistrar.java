package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;
import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleEventDefinitionRegistrar implements EventDefinitionRegistrar {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SimpleEventDefinitionRegistrar.class);
    private final Map<String, EventDefinition<?>> registry;

    public SimpleEventDefinitionRegistrar(
            List<EventDefinition<?>> definitions
    ) {
        this.registry = definitions.stream().collect(Collectors.toMap(
                eventDefinition -> eventDefinition.key().name(),
                Function.identity()
        ));

        log.debug("""
                EventDefinitionRegistrar Initialized with {} definitions""", definitions.size());
    }

    @Override
    public void registerDefinition(EventDefinition<?> definition) {
        EventKey key = definition.key();
        if (registry.containsKey(key.name())) {
            log.warn("""
                    Event definition registrar received register request for already existing key={}.
                    Ignore registration for this key""", key.name());
        }
        registry.put(key.name(), definition);
    }

    @Override
    public @Nullable <P extends EventPayload> EventDefinition<P> resolve(EventKey key) {
        var definition = registry.get(key.name());
        if (definition == null) return null;

        @SuppressWarnings("unchecked")
        var casted = (EventDefinition<P>) definition;

        return casted;
    }

    @Override
    public List<EventDefinition<?>> listAll() {
        return registry.values().stream().toList();
    }
}
