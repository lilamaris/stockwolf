package com.lilamaris.stockwolf.event.supports.kafka;

import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;
import com.lilamaris.stockwolf.event.foundation.DefaultEventTrace;
import com.lilamaris.stockwolf.event.foundation.EventDefinitionRegistry;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

public class JacksonEventCodec implements EventCodec {
    private final ObjectMapper objectMapper;
    private final EventDefinitionRegistry registry;

    public JacksonEventCodec(
            ObjectMapper objectMapper,
            EventDefinitionRegistry registry
    ) {
        this.objectMapper = objectMapper;
        this.registry = registry;
    }

    public DecodedEvent decode(String raw) {
        JsonNode root = objectMapper.readTree(raw);

        String eventKey = requiredField(root, "eventKey");
        Instant occurredAt = Instant.parse(requiredField(root, "occurredAt"));

        EventTrace trace = objectMapper.treeToValue(root.get("trace"), DefaultEventTrace.class);

        Class<? extends EventPayload> payloadType = registry.resolve(eventKey)
                .orElseThrow(() -> new IllegalArgumentException("Not support event key: " + eventKey));

        JsonNode payloadNode = root.get("payload");
        if (payloadNode == null || payloadNode.isNull()) {
            throw new IllegalArgumentException("payload is missing: " + eventKey);
        }

        EventPayload payload = objectMapper.treeToValue(payloadNode, payloadType);

        return new DecodedEvent(eventKey, occurredAt, trace, payload, raw);
    }

    private String requiredField(JsonNode root, String field) {
        JsonNode n = root.get(field);

        if (n == null || n.isNull() || n.asString().isBlank()) {
            throw new IllegalArgumentException("Missing field: " + field);
        }

        return n.asString();
    }
}
