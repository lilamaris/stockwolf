package com.lilamaris.stockwolf.event.core.relay.inbound;

public interface EventSubscriber {
    void dispatch(String raw) throws Exception;
}
