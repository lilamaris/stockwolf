package com.lilamaris.stockwolf.inventory.infrastructure.security.config;

import com.lilamaris.stockwolf.inventory.infrastructure.security.filter.IntrospectionAuthenticationFilter;
import com.lilamaris.stockwolf.inventory.infrastructure.security.util.IntrospectionProperties;
import com.lilamaris.stockwolf.inventory.infrastructure.security.util.Introspector;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
@Profile("!test")
@EnableConfigurationProperties(IntrospectionProperties.class)
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            Introspector introspector
    ) throws Exception {
        var introspectionAuthenticationFilter = new IntrospectionAuthenticationFilter(introspector);
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/health").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        introspectionAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    Introspector introspector(
            IntrospectionProperties properties
    ) {
        var web = WebClient.builder().baseUrl(properties.server().baseUrl()).build();
        return new Introspector(web, properties.api());
    }
}
