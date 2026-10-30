package com.finco.finco.security.gateway;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.finco.finco.entity.security.model.FincoScopes;
import com.finco.finco.infrastructure.config.authco.service.UserProvisionService;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.security.gateway.AuthSpringSecurityGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth spring security gateway test")
public class AuthSpringSecurityGatewayTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProvisionService userProvisionService;

    @InjectMocks
    private AuthSpringSecurityGateway authSpringSecurityGateway;

    private void authenticateWith(String... authorities) {
        Jwt jwt = Jwt.withTokenValue("token").header("alg", "RS256").subject("sub-1").build();
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthenticationToken(jwt, AuthorityUtils.createAuthorityList(authorities)));
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Plain scope name should match the granted SCOPE_ authority")
    public void hasScopeShouldMatchGrantedAuthority() {
        // Arrange
        authenticateWith("SCOPE_" + FincoScopes.PREMIUM);

        // Act & Assert
        assertTrue(authSpringSecurityGateway.hasScope(FincoScopes.PREMIUM));
        assertFalse(authSpringSecurityGateway.hasScope(FincoScopes.ADMIN));
    }

    @Test
    @DisplayName("Legacy admin scope should not grant finco:admin")
    public void legacyAdminScopeShouldNotMatch() {
        // Arrange
        authenticateWith("SCOPE_admin");

        // Act & Assert
        assertFalse(authSpringSecurityGateway.hasScope(FincoScopes.ADMIN));
    }

}
