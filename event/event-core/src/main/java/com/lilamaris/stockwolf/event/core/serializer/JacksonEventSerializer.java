package com.lilamaris.stockwolf.event.core.serializer;

import tools.jackson.databind.ObjectMapper;

public class JacksonEventSerializer implements EventSerializer {
    private final ObjectMapper objectMapper;

    public JacksonEventSerializer(
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String stringify(Object object) {
        return objectMapper.writeValueAsString(object);
    }
}
