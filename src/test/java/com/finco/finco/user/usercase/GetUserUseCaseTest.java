package com.finco.finco.user.usercase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.GetUserUseCase;

@ExtendWith(MockitoExtension.class)
public class GetUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private AuthGateway authGateway;

    private GetUserUseCase getUserUseCase;
    private User testUser;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        getUserUseCase = new GetUserUseCase(userGateway, authGateway);

        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setEnable(true);
    }

    @Test
    @DisplayName("Get existing user by ID successfully")
    public void getExistingUserByIdSuccess() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.of(testUser));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act
        User result = getUserUseCase.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Get non-existing user should throw UserNotFoundException")
    public void getNonExistingUserShouldThrowException() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.empty());
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            getUserUseCase.execute(userId);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Get user without permission should throw AccessDeniedBusinessException")
    public void getWithoutPermissionShouldThrowException() {
        // Arrange
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getUserUseCase.execute(userId);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(0)).findById(userId);
    }

}
