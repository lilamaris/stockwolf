package com.lilamaris.stockwolf.order.contract.event;

import com.lilamaris.stockwolf.event.core.outbound.EventDefinition;
import com.lilamaris.stockwolf.order.contract.event.definition.OrderCanceledEventDefinition;
import com.lilamaris.stockwolf.order.contract.event.definition.OrderCreatedEventDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderEventContractConfiguration {
    @Bean
    EventDefinition<?> orderCreatedDefinition() {
        return new OrderCreatedEventDefinition();
    }

    @Bean
    EventDefinition<?> orderCanceledDefinition() {
        return new OrderCanceledEventDefinition();
    }
}
