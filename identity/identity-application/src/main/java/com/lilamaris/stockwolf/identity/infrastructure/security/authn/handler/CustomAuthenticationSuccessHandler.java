package com.lilamaris.stockwolf.identity.infrastructure.security.authn.handler;

import com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token.AuthenticatedToken;
import com.lilamaris.stockwolf.identity.infrastructure.security.util.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ResponseWriter writer;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Authentication authentication) throws IOException {
        var tokenEntry = (AuthenticatedToken) authentication;
        Map<String, String> payload = new HashMap<>();
        payload.put("accessToken", tokenEntry.getAccessToken());
        payload.put("refreshToken", tokenEntry.getRefreshToken());
        writer.write(response, payload);
    }
}
