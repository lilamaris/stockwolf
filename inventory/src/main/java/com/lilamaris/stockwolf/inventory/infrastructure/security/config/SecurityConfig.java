package com.lilamaris.stockwolf.inventory.infrastructure.security.config;

import com.lilamaris.stockwolf.inventory.infrastructure.security.filter.IntrospectionAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            IntrospectionAuthenticationFilter introspectionAuthenticationFilter
    ) throws Exception {
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
}
