package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventKey;

public interface EventHeaderFactory {
    EventHeader build(EventKey eventKey);
}
