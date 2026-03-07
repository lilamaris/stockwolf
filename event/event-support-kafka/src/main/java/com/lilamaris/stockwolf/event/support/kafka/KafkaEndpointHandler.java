package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.inbound.EventRouter;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaEndpointHandler {
    private static final Logger log = LoggerFactory.getLogger(KafkaEndpointHandler.class);
    private final EventRouter eventRouter;
    private final EventCodec eventCodec;

    public KafkaEndpointHandler(
            EventRouter eventRouter,
            EventCodec eventCodec
    ) {
        this.eventRouter = eventRouter;
        this.eventCodec = eventCodec;
    }

    public void onMessage(ConsumerRecord<String, String> record) {
        var topic = record.topic();
        var partition = record.partition();
        var offset = record.offset();

        log.debug("""
                onMessage record topic={}, partition={}, offset={}, payload={}""", topic, partition, offset, record.value());

        try {
//            eventRouter.route(record);
        } catch (Exception e) {
            throw e;
        }
    }
}
