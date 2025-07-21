package com.finco.finco.user.usercase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.GetAllUserUseCase;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get all user test")
public class GetAllUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private AuthGateway authGateway;

    private GetAllUserUseCase getAllUserUseCase;
    private com.finco.finco.entity.pagination.PageRequest pageRequest;
    private com.finco.finco.entity.pagination.PagedResult<User> pagedResult;
    private User user1, user2;

    @BeforeEach
    public void setUp() {
        getAllUserUseCase = new GetAllUserUseCase(userGateway, authGateway);
        pageRequest = new com.finco.finco.entity.pagination.PageRequest(0, 10, "name", "asc");

        user1 = new User();
        user1.setId(1L);
        user1.setName("User One");
        user1.setEmail("user1@example.com");

        user2 = new User();
        user2.setId(2L);
        user2.setName("User Two");
        user2.setEmail("user2@example.com");

        pagedResult = new com.finco.finco.entity.pagination.PagedResult<User>(List.of(user1, user2), 2, 1, 0, 10, true,
                false, false, false);
    }

    @Test
    @DisplayName("Get all users successfully with admin role")
    public void getAllUsersSuccess() {
        // Arrange
        when(authGateway.isAuthenticatedUserInRole("ADMIN")).thenReturn(true);
        when(userGateway.findAll(any(com.finco.finco.entity.pagination.PageRequest.class))).thenReturn(pagedResult);

        // Act
        PagedResult<User> result = getAllUserUseCase.execute(pageRequest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(authGateway, times(1)).isAuthenticatedUserInRole("ADMIN");
        verify(userGateway, times(1)).findAll(any(com.finco.finco.entity.pagination.PageRequest.class));
    }

    @Test
    @DisplayName("Get all users without admin role should throw AccessDeniedBusinessException")
    public void getAllUsersWithoutAdminRoleShouldThrowException() {
        // Arrange
        when(authGateway.isAuthenticatedUserInRole("ADMIN")).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedBusinessException.class, () -> {
            getAllUserUseCase.execute(pageRequest);
        });

        verify(authGateway, times(1)).isAuthenticatedUserInRole("ADMIN");
        verify(userGateway, never()).findAll(any(com.finco.finco.entity.pagination.PageRequest.class));
    }

}
