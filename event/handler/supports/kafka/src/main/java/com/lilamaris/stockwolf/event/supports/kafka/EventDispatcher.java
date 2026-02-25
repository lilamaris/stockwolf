package com.lilamaris.stockwolf.event.supports.kafka;

public interface EventDispatcher {
    void dispatch(String raw) throws Exception;
}
