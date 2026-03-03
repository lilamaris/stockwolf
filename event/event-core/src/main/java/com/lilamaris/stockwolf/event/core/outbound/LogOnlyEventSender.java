package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogOnlyEventSender implements EventSender {
    private static final Logger log = LoggerFactory.getLogger(LogOnlyEventSender.class);

    @Override
    public void send(String topic, EventKey eventKey, String raw) {
        log.info("""
                Sending event message:
                topic={}, eventKey={}, raw={}
                """, topic, eventKey.name(), raw);
    }
}
