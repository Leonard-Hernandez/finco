package com.finco.finco.infrastructure.config.security.jwt;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

@DisplayName("Jwt validator test")
public class JwtValidatorTest {

    private static final String ISSUER = "http://finco.localhost/authco";
    private static final String RESOURCE_ID = "http://finco.localhost/finco-api/v1";

    private final OAuth2TokenValidator<Jwt> validator = JwtDecoderConfig.jwtValidator(ISSUER, RESOURCE_ID);

    private Jwt jwt(String issuer, List<String> audience, Instant expiresAt) {
        return Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject("sub-1")
                .issuer(issuer)
                .audience(audience)
                .issuedAt(expiresAt.minusSeconds(600))
                .expiresAt(expiresAt)
                .build();
    }

    @Test
    @DisplayName("Valid issuer and audience should pass")
    public void validTokenShouldPass() {
        // Arrange
        Jwt token = jwt(ISSUER, List.of("finco-client", RESOURCE_ID), Instant.now().plusSeconds(300));

        // Act
        OAuth2TokenValidatorResult result = validator.validate(token);

        // Assert
        assertFalse(result.hasErrors());
    }

    @Test
    @DisplayName("Internal issuer should be rejected")
    public void wrongIssuerShouldFail() {
        // Arrange
        Jwt token = jwt("http://authco:8087/authco", List.of(RESOURCE_ID), Instant.now().plusSeconds(300));

        // Act
        OAuth2TokenValidatorResult result = validator.validate(token);

        // Assert
        assertTrue(result.hasErrors());
    }

    @Test
    @DisplayName("Missing finco audience should be rejected")
    public void missingAudienceShouldFail() {
        // Arrange
        Jwt token = jwt(ISSUER, List.of("another-api"), Instant.now().plusSeconds(300));

        // Act
        OAuth2TokenValidatorResult result = validator.validate(token);

        // Assert
        assertTrue(result.hasErrors());
        assertTrue(result.getErrors().stream().anyMatch(error -> error.getDescription().contains("aud")));
    }

    @Test
    @DisplayName("Expired token should be rejected")
    public void expiredTokenShouldFail() {
        // Arrange
        Jwt token = jwt(ISSUER, List.of(RESOURCE_ID), Instant.now().minusSeconds(3600));

        // Act
        OAuth2TokenValidatorResult result = validator.validate(token);

        // Assert
        assertTrue(result.hasErrors());
    }

}
