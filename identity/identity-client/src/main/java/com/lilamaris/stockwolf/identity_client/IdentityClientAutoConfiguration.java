package com.lilamaris.stockwolf.identity_client;

import com.lilamaris.stockwolf.identity_client.core.Introspector;
import com.lilamaris.stockwolf.identity_client.foundation.web.IntrospectionClientProperties;
import com.lilamaris.stockwolf.identity_client.foundation.web.IntrospectorWebClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "spring.introspection",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(IntrospectionClientProperties.class)
public class IdentityClientAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    WebClient introspectionWebClient(
            IntrospectionClientProperties properties
    ) {
        return WebClient.builder()
                .baseUrl(properties.server().baseUrl())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    Introspector introspector(
            WebClient introspectionWebClient,
            IntrospectionClientProperties properties
    ) {
        return new IntrospectorWebClient(
                introspectionWebClient,
                properties.api()
        );
    }
}
