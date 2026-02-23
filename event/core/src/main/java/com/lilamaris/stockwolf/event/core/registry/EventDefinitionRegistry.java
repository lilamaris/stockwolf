package com.lilamaris.stockwolf.event.core.registry;

import com.lilamaris.stockwolf.event.core.EventPayload;

import javax.naming.OperationNotSupportedException;

public interface EventDefinitionRegistry {
    Class<? extends EventPayload> resolve(String eventKey) throws OperationNotSupportedException;
}
