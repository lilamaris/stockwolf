package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface EventDefinitionRegistrar {
    void registerDefinition(EventDefinition<?> definition);

    <P extends EventPayload> @Nullable EventDefinition<P> resolve(EventKey key);

    List<EventDefinition<?>> listAll();
}
