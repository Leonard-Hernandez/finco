package com.finco.finco.infrastructure.config.security.jwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * Single JwtDecoder shared by the HTTP resource server and the STOMP interceptor.
 * Keys are fetched lazily from the internal authco URL (no discovery at startup),
 * while the issuer is validated against the public value.
 */
@Configuration
public class JwtDecoderConfig {

    @Bean
    JwtDecoder jwtDecoder(@Value("${finco.security.internal-url}") String authcoInternalUrl,
            @Value("${finco.security.issuer}") String issuer,
            @Value("${finco.security.resource-id}") String resourceId) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(authcoInternalUrl + "/oauth2/jwks").build();
        jwtDecoder.setJwtValidator(jwtValidator(issuer, resourceId));
        return jwtDecoder;
    }

    /**
     * Timestamp + issuer + audience validation.
     */
    static OAuth2TokenValidator<Jwt> jwtValidator(String issuer, String resourceId) {
        return new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuer),
                new JwtClaimValidator<List<String>>(JwtClaimNames.AUD, aud -> aud != null && aud.contains(resourceId)));
    }

}
