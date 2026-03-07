package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.core.store.EventFlow;
import com.lilamaris.stockwolf.event.core.store.EventStore;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaEndpointHandler {
    private static final Logger log = LoggerFactory.getLogger(KafkaEndpointHandler.class);
    private final EventStore eventStore;
    private final EventCodec eventCodec;

    public KafkaEndpointHandler(
            EventStore eventStore,
            EventCodec eventCodec
    ) {
        this.eventStore = eventStore;
        this.eventCodec = eventCodec;
    }

    public void onMessage(ConsumerRecord<String, String> record) {
        var topic = record.topic();
        var partition = record.partition();
        var offset = record.offset();

        log.debug("""
                onMessage record topic={}, partition={}, offset={}, payload={}""", topic, partition, offset, record.value());

        var eventEnvelope = eventCodec.decode(record.value());
        eventStore.accept(eventEnvelope, EventFlow.INBOUND);
    }
}
