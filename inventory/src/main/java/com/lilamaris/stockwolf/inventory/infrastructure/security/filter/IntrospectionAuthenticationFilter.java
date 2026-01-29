package com.lilamaris.stockwolf.inventory.infrastructure.security.filter;

import com.lilamaris.stockwolf.inventory.infrastructure.security.util.IntrospectionResult;
import com.lilamaris.stockwolf.inventory.infrastructure.security.util.Introspector;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class IntrospectionAuthenticationFilter extends OncePerRequestFilter {
    private final Introspector introspector;
    private final String authorizationPrefix = "Bearer";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var token = getBearerToken(request);

        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        var introspection = introspector.introspect(token.get());

        if (!introspection.active()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        var auth = getAuth(introspection);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private Optional<String> getBearerToken(
            @NonNull HttpServletRequest request
    ) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(authorizationPrefix))
                .map(header -> header.substring(authorizationPrefix.length()).trim());
    }

    private Authentication getAuth(IntrospectionResult introspection) {
        var authorities = Arrays.stream(introspection.summary().scope().split(" "))
                .map(s -> new SimpleGrantedAuthority("SCOPE_" + s))
                .toList();

        return new UsernamePasswordAuthenticationToken(
                introspection.summary().subject(),
                null,
                authorities
        );
    }
}
