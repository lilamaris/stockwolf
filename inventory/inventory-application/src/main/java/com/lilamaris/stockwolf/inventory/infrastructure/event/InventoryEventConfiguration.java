package com.lilamaris.stockwolf.inventory.infrastructure.event;

import com.lilamaris.stockwolf.inventory.infrastructure.event.listener.OrderCreatedListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryEventConfiguration {
    @Bean
    OrderCreatedListener orderCreatedListener() {
        return new OrderCreatedListener();
    }
}
