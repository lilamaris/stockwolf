package com.lilamaris.stockwolf.event.core.serializer;

import com.lilamaris.stockwolf.event.core.*;
import com.lilamaris.stockwolf.event.core.factory.EventEnvelopeFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class JacksonEventCodec implements EventCodec {
    private final ObjectMapper objectMapper;
    private final EventEnvelopeFactory eventEnvelopeFactory;

    public JacksonEventCodec(
            ObjectMapper objectMapper,
            EventEnvelopeFactory eventEnvelopeFactory
    ) {
        this.objectMapper = objectMapper;
        this.eventEnvelopeFactory = eventEnvelopeFactory;
    }

    @Override
    public EventEnvelope decode(String raw) {
        JsonNode root = objectMapper.readTree(raw);

        JsonNode headerNode = requiredNode(root, "header");
        JsonNode traceNode = requiredNode(root, "trace");
        JsonNode rawPayloadNode = requiredNode(root, "rawPayload");

        EventHeader eventHeader = objectMapper.treeToValue(headerNode, SimpleEventHeader.class);
        EventTrace eventTrace = objectMapper.treeToValue(traceNode, SimpleEventTrace.class);

        String rawPayload = rawPayloadNode.isString()
                ? rawPayloadNode.asString()
                : rawPayloadNode.toString();

        return eventEnvelopeFactory.build(eventHeader, eventTrace, rawPayload);
    }

    @Override
    public String encode(EventHeader header, EventTrace trace, String rawPayload) {
        ObjectNode root = objectMapper.createObjectNode();

        root.set("header", objectMapper.valueToTree(header));
        root.set("trace", objectMapper.valueToTree(trace));
        root.put("payload", rawPayload);

        return objectMapper.writeValueAsString(root);
    }

    private JsonNode requiredNode(JsonNode root, String field) {
        JsonNode n = root.get(field);
        if (n == null || n.isNull()) {
            throw new IllegalArgumentException("Missing field: " + field);
        }
        return n;
    }
}
