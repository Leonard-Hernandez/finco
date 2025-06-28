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
import com.finco.finco.usecase.user.DeleteUserUseCase;

@ExtendWith(MockitoExtension.class)
public class DeleteUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private AuthGateway authGateway;

    private DeleteUserUseCase deleteUserUseCase;
    private User testUser;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        deleteUserUseCase = new DeleteUserUseCase(userGateway, authGateway);

        testUser = new User();
        testUser.setId(userId);
        testUser.setName("User to Delete");
        testUser.setEmail("delete@example.com");
        testUser.setPassword("password");
        testUser.setEnable(true);
    }

    @Test
    @DisplayName("Delete existing user successfully")
    public void deleteExistingUserSuccess() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.of(testUser));
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);
        when(userGateway.delete(testUser)).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setEnable(false);
            return user;
        });

        // Act
        User result = deleteUserUseCase.excute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(false, result.isEnable());
        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, times(1)).delete(testUser);
    }

    @Test
    @DisplayName("Delete non-existing user should throw UserNotFoundException")
    public void deleteNonExistingUserShouldThrowException() {
        // Arrange
        when(userGateway.findById(userId)).thenReturn(Optional.empty());
        doNothing().when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            deleteUserUseCase.excute(userId);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("Delete user without permission should throw AccessDeniedBusinessException")
    public void deleteUserWithoutPermissionShouldThrowException() {
        // Arrange
        doThrow(AccessDeniedBusinessException.class).when(authGateway).verifyOwnershipOrAdmin(userId);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            deleteUserUseCase.excute(userId);
        });

        verify(authGateway, times(1)).verifyOwnershipOrAdmin(userId);
        verify(userGateway, times(0)).findById(userId);
        verify(userGateway, never()).delete(any(User.class));
    }

}
