package com.finco.finco.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.security.exception.IdentityProviderUnavailableException;
import com.finco.finco.entity.security.exception.InvalidTokenException;
import com.finco.finco.infrastructure.config.authco.gateway.AuthcoRestGateway;
import com.finco.finco.infrastructure.config.authco.service.UserProvisionService;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("User provision test")
public class UserProvisionServiceTest {

    private static final String SUB = "sub-1";
    private static final String TOKEN = "token";

    @Mock
    private AuthcoRestGateway authcoRestGateway;

    @Mock
    private UserRepository userRepository;

    private UserProvisionService userProvisionService;

    @BeforeEach
    public void setUp() {
        userProvisionService = new UserProvisionService(authcoRestGateway, userRepository);
    }

    @Test
    @DisplayName("Rejected token should propagate InvalidTokenException without touching users")
    public void rejectedTokenShouldThrowInvalidToken() {
        // Arrange
        when(authcoRestGateway.getUserInfo(TOKEN)).thenThrow(new InvalidTokenException());

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> userProvisionService.provisionUser(SUB, TOKEN));

        verify(userRepository, never()).findByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Unavailable authco should propagate IdentityProviderUnavailableException without touching users")
    public void unavailableAuthcoShouldThrowUnavailable() {
        // Arrange
        when(authcoRestGateway.getUserInfo(TOKEN)).thenThrow(new IdentityProviderUnavailableException());

        // Act & Assert
        assertThrows(IdentityProviderUnavailableException.class,
                () -> userProvisionService.provisionUser(SUB, TOKEN));

        verify(userRepository, never()).findByEmail(any());
        verify(userRepository, never()).save(any());
    }

}
