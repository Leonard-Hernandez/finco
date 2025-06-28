package com.finco.finco.user.usercase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import com.finco.finco.usecase.user.UpdateUserUseCase;
import com.finco.finco.usecase.user.dto.IUserUpdateData;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private AuthGateway authGateway;

    @Mock
    private IUserUpdateData userUpdateData;

    private UpdateUserUseCase updateUserUseCase;
    private User existingUser;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        updateUserUseCase = new UpdateUserUseCase(userGateway, authGateway);

        existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Original Name");
        existingUser.setEmail("original@example.com");
        existingUser.setPassword("password");
        existingUser.setEnable(true);
    }

    @Test
    @DisplayName("Update user name successfully")
    public void updateUserNameSuccess() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(userUpdateData.name()).thenReturn("Updated Name");
        when(userUpdateData.enable()).thenReturn(null);
        when(userGateway.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = updateUserUseCase.execute(userId, userUpdateData);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("original@example.com", result.getEmail()); // Email should not change
        assertTrue(result.isEnable()); // Enable status should not change
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, times(1)).update(any(User.class));
    }

    @Test
    @DisplayName("Update user enable status successfully")
    public void updateUserEnableStatusSuccess() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(userUpdateData.name()).thenReturn(null);
        when(userUpdateData.enable()).thenReturn(false);
        when(userGateway.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = updateUserUseCase.execute(userId, userUpdateData);

        // Assert
        assertNotNull(result);
        assertEquals("Original Name", result.getName()); // Name should not change
        assertFalse(result.isEnable()); // Enable status should be updated
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, times(1)).update(any(User.class));
    }

    @Test
    @DisplayName("Update non-existing user should throw UserNotFoundException")
    public void updateNonExistingUserShouldThrowException() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.empty());
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            updateUserUseCase.execute(userId, userUpdateData);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, never()).update(any(User.class));
    }

    @Test
    @DisplayName("Update user without permission should throw AccessDeniedBusinessException")
    public void updateUserWithoutPermissionShouldThrowException() {
        // Arrange
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            updateUserUseCase.execute(userId, userUpdateData);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(0)).findById(userId);
        verify(userGateway, never()).update(any(User.class));
    }

}
