package com.finco.finco.infrastructure.config.security.entrypoint;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@DisplayName("Resource metadata access denied handler test")
public class ResourceMetadataAccessDeniedHandlerTest {

    private static final String RESOURCE_METADATA_URL = "http://finco.localhost/finco-api/v1/.well-known/oauth-protected-resource";

    private final ResourceMetadataAccessDeniedHandler handler = new ResourceMetadataAccessDeniedHandler(
            RESOURCE_METADATA_URL,
            Map.of(PathPatternRequestMatcher.withDefaults().matcher("/sse/**"), "finco:premium"));

    private MockHttpServletResponse handle(String path) {
        MockHttpServletResponse response = new MockHttpServletResponse();
        handler.handle(new MockHttpServletRequest("GET", path), response, new AccessDeniedException("denied"));
        return response;
    }

    @Test
    @DisplayName("Scope-protected path should challenge with the required scope")
    public void scopeProtectedPathShouldIncludeScope() {
        // Act
        MockHttpServletResponse response = handle("/sse");

        // Assert
        assertEquals(403, response.getStatus());
        assertEquals("Bearer error=\"insufficient_scope\", "
                + "error_description=\"The request requires higher privileges than provided by the access token.\", "
                + "scope=\"finco:premium\", "
                + "resource_metadata=\"" + RESOURCE_METADATA_URL + "\"",
                response.getHeader(HttpHeaders.WWW_AUTHENTICATE));
    }

    @Test
    @DisplayName("Path without a required scope should omit the scope parameter")
    public void unscopedPathShouldOmitScope() {
        // Act
        MockHttpServletResponse response = handle("/accounts");

        // Assert
        assertEquals(403, response.getStatus());
        String challenge = response.getHeader(HttpHeaders.WWW_AUTHENTICATE);
        assertTrue(challenge.startsWith("Bearer error=\"insufficient_scope\""));
        assertFalse(challenge.contains("scope=\"finco"));
        assertTrue(challenge.endsWith("resource_metadata=\"" + RESOURCE_METADATA_URL + "\""));
    }

}
