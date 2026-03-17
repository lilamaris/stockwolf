package com.lilamaris.stockwolf.inventory.infrastructure.event;

import com.lilamaris.stockwolf.event.core.inbound.EventListenerRegistrar;
import com.lilamaris.stockwolf.event.core.inbound.EventRouter;
import com.lilamaris.stockwolf.event.core.inbound.SimpleEventRouter;
import com.lilamaris.stockwolf.event.core.serializer.EventDeserializer;
import com.lilamaris.stockwolf.inventory.infrastructure.event.listener.OrderCanceledEventListener;
import com.lilamaris.stockwolf.inventory.infrastructure.event.listener.OrderCreatedEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryEventConfiguration {
    @Bean
    OrderCreatedEventListener orderCreatedListener() {
        return new OrderCreatedEventListener();
    }

    @Bean
    OrderCanceledEventListener orderCanceledEventListener() {
        return new OrderCanceledEventListener();
    }

    @Bean
    EventRouter eventRouter(
            EventListenerRegistrar eventListenerRegistrar,
            EventDeserializer eventDeserializer
    ) {
        return new SimpleEventRouter(
                eventListenerRegistrar,
                eventDeserializer
        );
    }
}
