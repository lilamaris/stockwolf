package com.lilamaris.stockwolf.event.core.serializer;

import com.lilamaris.stockwolf.event.core.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class JacksonEventCodec implements EventCodec, PayloadSerializer, PayloadDeserializer {
    private final ObjectMapper objectMapper;

    public JacksonEventCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public EventEnvelope decode(String raw) {
        JsonNode root = objectMapper.readTree(raw);
        String rawHeader = requiredField(root, "header");
        String rawTrace = requiredField(root, "trace");
        String rawPayload = requiredField(root, "payload");

        EventHeader eventHeader = objectMapper.readValue(rawHeader, SimpleEventHeader.class);
        EventTrace eventTrace = objectMapper.readValue(rawTrace, SimpleEventTrace.class);
        return new SimpleEventEnvelope(eventHeader, eventTrace, rawPayload);
    }

    @Override
    public String encode(EventHeader header, EventTrace trace, String rawPayload) {
        ObjectNode root = objectMapper.createObjectNode();

        root.set("header", objectMapper.valueToTree(header));
        root.set("trace", objectMapper.valueToTree(trace));
        root.put("payload", rawPayload);

        return objectMapper.writeValueAsString(root);
    }

    @Override
    public <P extends EventPayload> P materialize(String rawPayload, Class<P> payloadType) {
        return objectMapper.readValue(rawPayload, payloadType);
    }

    @Override
    public <P extends EventPayload> String stringify(P payload) {
        return objectMapper.writeValueAsString(payload);
    }

    private String requiredField(JsonNode root, String field) {
        JsonNode n = root.get(field);

        if (n == null || n.isNull() || n.asString().isBlank()) {
            throw new IllegalArgumentException("Missing field: " + field);
        }

        return n.asString();
    }
}
