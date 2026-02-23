package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.registry.EventDefinition;
import com.lilamaris.stockwolf.event.core.registry.EventDefinitionRegistry;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultEventDefinitionRegistry implements EventDefinitionRegistry {
    private final Map<String, Class<? extends EventPayload>> registry;

    public DefaultEventDefinitionRegistry(List<EventDefinition<?>> definitions) {
        this.registry = definitions.stream().collect(Collectors.toUnmodifiableMap(
                EventDefinition::key,
                EventDefinition::payload
        ));
    }

    public Class<? extends EventPayload> resolve(String eventKey) throws OperationNotSupportedException {
        if (registry.containsKey(eventKey)) {
            return registry.get(eventKey);
        } else {
            throw new OperationNotSupportedException(String.format(
                    "Not support definition of key %s", eventKey
            ));
        }
    }
}
