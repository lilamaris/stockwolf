package com.lilamaris.stockwolf.event.core.relay.ingoing;

public interface EventDispatcher {
    void dispatch(String raw) throws Exception;
}
