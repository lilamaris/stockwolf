package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;
import org.jspecify.annotations.Nullable;

public interface EventListenerRegistrar {
    void registerListener(EventListener<?> listener);

    <P extends EventPayload> @Nullable EventListener<P> resolve(EventKey key);
}
