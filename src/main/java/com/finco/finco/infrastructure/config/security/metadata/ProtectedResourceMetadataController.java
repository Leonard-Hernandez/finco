package com.finco.finco.infrastructure.config.security.metadata;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.security.model.FincoScopes;
import com.finco.finco.infrastructure.config.security.metadata.dto.ProtectedResourceMetadataDto;

/**
 * Serves the RFC 9728 metadata so MCP clients can discover the authorization server.
 * Also answers the path-suffixed variants (e.g. {@code /.well-known/oauth-protected-resource/sse}).
 */
@RestController
public class ProtectedResourceMetadataController {

    public static final String PATH = "/.well-known/oauth-protected-resource";

    /** OpenID Connect scopes authco grants alongside the finco scopes (sign-in and userinfo). */
    private static final List<String> OIDC_SCOPES = List.of("openid", "profile", "email");

    private final ProtectedResourceMetadataDto metadata;

    public ProtectedResourceMetadataController(@Value("${finco.security.resource-id}") String resourceId,
            @Value("${finco.security.issuer}") String issuer) {
        List<String> scopesSupported = Stream.concat(OIDC_SCOPES.stream(), FincoScopes.CLIENT_REQUESTABLE.stream())
                .toList();
        this.metadata = new ProtectedResourceMetadataDto(resourceId, List.of(issuer), scopesSupported,
                List.of("header"));
    }

    @GetMapping(value = { PATH, PATH + "/**" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProtectedResourceMetadataDto> getMetadata() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofHours(1)).cachePublic())
                .body(metadata);
    }

}
