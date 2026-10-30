package com.finco.finco.infrastructure.config.security.entrypoint;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Bearer access denied handler that answers 403 with an RFC 6750 {@code insufficient_scope}
 * challenge, the {@code scope} required by the matched rule and the RFC 9728
 * {@code resource_metadata} parameter, so clients can step up their token.
 */
public class ResourceMetadataAccessDeniedHandler implements AccessDeniedHandler {

    private static final String ERROR_DESCRIPTION = "The request requires higher privileges than provided by the access token.";

    private final String resourceMetadataUrl;
    private final Map<RequestMatcher, String> requiredScopes;

    /**
     * @param requiredScopes scope required by each protected path, checked in iteration order
     */
    public ResourceMetadataAccessDeniedHandler(String resourceMetadataUrl, Map<RequestMatcher, String> requiredScopes) {
        this.resourceMetadataUrl = resourceMetadataUrl;
        this.requiredScopes = new LinkedHashMap<>(requiredScopes);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) {
        StringBuilder challenge = new StringBuilder("Bearer error=\"insufficient_scope\", error_description=\"")
                .append(ERROR_DESCRIPTION).append('"');

        // Paths that are only authenticated() have no scope to step up to
        String requiredScope = requiredScope(request);
        if (requiredScope != null) {
            challenge.append(", scope=\"").append(requiredScope).append('"');
        }
        challenge.append(", resource_metadata=\"").append(resourceMetadataUrl).append('"');

        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, challenge.toString());
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

    private String requiredScope(HttpServletRequest request) {
        return requiredScopes.entrySet().stream()
                .filter(entry -> entry.getKey().matches(request))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

}
