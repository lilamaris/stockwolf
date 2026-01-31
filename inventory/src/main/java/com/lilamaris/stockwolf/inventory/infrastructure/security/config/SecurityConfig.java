package com.lilamaris.stockwolf.inventory.infrastructure.security.config;

import com.lilamaris.stockwolf.identity_client.core.Introspector;
import com.lilamaris.stockwolf.identity_client.spring_security.BearerTokenAuthenticationFilter;
import com.lilamaris.stockwolf.identity_client.spring_security.IntrospectionAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@Profile("!test")
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            Introspector introspector
    ) throws Exception {
        var provider = new IntrospectionAuthenticationProvider(introspector);
        var authenticationManager = new ProviderManager(provider);
        var bearerFilter = new BearerTokenAuthenticationFilter(authenticationManager);

        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/health").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(bearerFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
