package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventBinding;
import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.EventHandler;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventRegistry {
    private final Map<EventKey, EventBinding> registry;
    private final EventCodec codec;

    public EventRegistry(
            List<EventDefinition<?>> definitions,
            List<EventHandler<?>> handlers,
            EventCodec codec
    ) {
        this.codec = codec;

        var defMap = definitions.stream().collect(Collectors.toUnmodifiableMap(
                EventDefinition::key,
                Function.identity()
        ));

        var handlerMap = handlers.stream().collect(Collectors.toUnmodifiableMap(
                EventHandler::key,
                Function.identity()
        ));

        if (!defMap.keySet().equals(handlerMap.keySet())) {
            throw new IllegalStateException(String.format(
                    "Definitions and handlers keys mismatch: def=%s, handler=%s",
                    defMap.keySet(),
                    handlerMap.keySet()
            ));
        }

        Map<EventKey, EventBinding> tmp = new HashMap<>();
        for (var key : defMap.keySet()) {
            EventDefinition<?> d = defMap.get(key);
            EventHandler<?> h = handlerMap.get(key);

            tmp.put(key, bindChecked(d, h));
        }
        this.registry = Map.copyOf(tmp);
    }

    private <P extends EventPayload> EventBinding bindChecked(
            EventDefinition<?> d,
            EventHandler<?> h
    ) {
        if (!d.payload().equals(h.payload())) {
            throw new IllegalStateException("Payload type mismatch for name=" + d.key()
                    + " def=" + d.payload().getName()
                    + " handler=" + h.payload().getName());
        }
        @SuppressWarnings("unchecked")
        EventDefinition<P> dd = (EventDefinition<P>) d;
        @SuppressWarnings("unchecked")
        EventHandler<P> hh = (EventHandler<P>) h;
        return new TypedEventBinding<P>(dd, hh, codec);
    }

    public Optional<EventBinding> resolve(EventKey eventKey) {
        return Optional.ofNullable(registry.get(eventKey));
    }
}
