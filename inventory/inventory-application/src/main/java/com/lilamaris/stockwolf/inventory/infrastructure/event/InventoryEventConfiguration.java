package com.lilamaris.stockwolf.inventory.infrastructure.event;

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
}
