package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventKey;

public interface EventSender {
    void send(String topic, EventKey eventKey, String raw);
}

