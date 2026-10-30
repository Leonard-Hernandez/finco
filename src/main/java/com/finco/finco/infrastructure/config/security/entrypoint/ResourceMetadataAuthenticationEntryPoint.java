package com.finco.finco.infrastructure.config.security.entrypoint;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Bearer entry point that adds the RFC 9728 {@code resource_metadata} parameter
 * to the {@code WWW-Authenticate} challenge.
 */
public class ResourceMetadataAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final BearerTokenAuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final String resourceMetadataParameter;

    public ResourceMetadataAuthenticationEntryPoint(String resourceMetadataUrl) {
        this.resourceMetadataParameter = "resource_metadata=\"" + resourceMetadataUrl + "\"";
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        delegate.commence(request, response, authException);

        // The delegate always writes a Bearer challenge, bare or followed by error parameters
        String challenge = response.getHeader(HttpHeaders.WWW_AUTHENTICATE);
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer".equals(challenge)
                ? "Bearer " + resourceMetadataParameter
                : challenge + ", " + resourceMetadataParameter);
    }

}
