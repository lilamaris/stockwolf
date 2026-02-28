package com.lilamaris.stockwolf.event.foundation.serializer;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.foundation.DefaultEventHeader;
import com.lilamaris.stockwolf.event.foundation.DefaultEventKey;
import com.lilamaris.stockwolf.event.foundation.DefaultEventTrace;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.time.Instant;

public class JacksonEventCodec implements EventCodec {
    private final ObjectMapper objectMapper;

    public JacksonEventCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public EventHeader decodeHeader(String raw) {
        JsonNode root = objectMapper.readTree(raw);

        EventKey eventKey = DefaultEventKey.of(requiredField(root, "eventKey"));
        Instant occurredAt = Instant.parse(requiredField(root, "occurredAt"));

        return new DefaultEventHeader(eventKey, occurredAt);
    }

    @Override
    public EventTrace decodeTrace(String raw) {
        JsonNode root = objectMapper.readTree(raw);
        return objectMapper.treeToValue(root.get("trace"), DefaultEventTrace.class);
    }

    @Override
    public <P extends EventPayload> P decodePayload(String raw, Class<P> payloadType) {
        JsonNode root = objectMapper.readTree(raw);
        JsonNode payloadNode = root.get("payload");

        if (payloadNode == null || payloadNode.isNull()) {
            throw new IllegalArgumentException("payload is missing");
        }

        return objectMapper.treeToValue(payloadNode, payloadType);
    }

    @Override
    public <P extends EventPayload> String encodePayload(P payload) {
        return objectMapper.writeValueAsString(payload);
    }

    @Override
    public String encode(EventHeader header, EventTrace trace, String raw) {
        ObjectNode root = objectMapper.createObjectNode();

        root.put("eventKey", header.eventKey().name());
        root.put("occurredAt", header.occurredAt().toString());

        JsonNode traceNode = objectMapper.valueToTree(trace);
        JsonNode payloadNode = objectMapper.valueToTree(raw);

        root.set("trace", traceNode);
        root.set("payload", payloadNode);

        return objectMapper.writeValueAsString(root);
    }

    private String requiredField(JsonNode root, String field) {
        JsonNode n = root.get(field);

        if (n == null || n.isNull() || n.asString().isBlank()) {
            throw new IllegalArgumentException("Missing field: " + field);
        }

        return n.asString();
    }
}
