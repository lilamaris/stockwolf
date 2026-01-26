package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token.CredentialSignInAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JsonCredentialSignInProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final RequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/auth/signIn");

    private final ObjectMapper mapper;

    public JsonCredentialSignInProcessingFilter(ObjectMapper mapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response
    ) throws AuthenticationException {
        try {
            var body = mapper.readValue(request.getInputStream(), CredentialRequest.SignIn.class);
            var email = body.email();
            var rawPassword = body.password();
            var token = new CredentialSignInAuthentication(email, rawPassword);
            return this.getAuthenticationManager().authenticate(token);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Invalid Request Body");
        }
    }
}
