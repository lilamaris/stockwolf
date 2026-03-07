package com.lilamaris.stockwolf.event.core.serializer;

import com.lilamaris.stockwolf.event.core.EventPayload;
import tools.jackson.databind.ObjectMapper;

public class JacksonEventDeserializer implements EventDeserializer {
    private final ObjectMapper objectMapper;

    public JacksonEventDeserializer(
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <P extends EventPayload> P materialize(String rawPayload, Class<P> payloadType) {
        return objectMapper.readValue(rawPayload, payloadType);
    }
}
