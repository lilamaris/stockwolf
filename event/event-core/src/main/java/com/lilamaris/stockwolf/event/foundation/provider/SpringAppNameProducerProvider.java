package com.lilamaris.stockwolf.event.foundation.provider;

import com.lilamaris.stockwolf.event.core.provider.ProducerProvider;
import org.springframework.core.env.Environment;

public class SpringAppNameProducerProvider implements ProducerProvider {
    private final Environment env;

    public SpringAppNameProducerProvider(Environment env) {
        this.env = env;
    }

    @Override
    public String producer() {
        String name = env.getProperty("spring.application.name");
        return (name == null || name.isBlank()) ? "unknown-service" : name;
    }
}
