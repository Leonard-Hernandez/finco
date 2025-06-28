package com.finco.finco.user.usercase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.CreateUserAdminUseCase;
import com.finco.finco.usecase.user.dto.IUserRegistrationData;

@ExtendWith(MockitoExtension.class)
public class CreateUserAdminTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private RoleGateway roleGateway;

    @Mock
    private AuthGateway authGateway;

    private CreateUserAdminUseCase createUserUseCase;
    private IUserRegistrationData userData;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    public void setUp() {
        createUserUseCase = new CreateUserAdminUseCase(userGateway, roleGateway, authGateway);
        userData = new IUserRegistrationData() {
            @Override
            public String name() {
                return "pepe";
            }

            @Override
            public String email() {
                return "pepe@pepe.com";
            }

            @Override
            public String password() {
                return "123";
            }
        };
        userRole = new Role();
        userRole.setName("ROLE_USER");
        adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Create user admin success")
    public void createUserAdminSuccess() {
        // Arrange
        when(authGateway.isAuthenticatedUserInRole("ADMIN")).thenReturn(true);
        when(roleGateway.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(roleGateway.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        when(userGateway.create(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        User user = new User();
        user.setId(1L);
        user.setName("pepe");
        user.setEmail("pepe@pepe.com");
        user.setPassword("123");
        user.setEnable(true);

        user.setRoles(List.of(userRole, adminRole));

        // Act
        User result = createUserUseCase.execute(userData);

        // Assert
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.isEnable(), result.isEnable());
        assertNotNull(result.getRegistrationDate());
        assertEquals(user.getRoles(), result.getRoles());
    }

    @Test
    @DisplayName("Create user admin not admin")
    public void createUserAdminNotAdmin() {
        // Arrange
        when(authGateway.isAuthenticatedUserInRole("ADMIN")).thenReturn(false);

        // Act and Assert
        assertThrows(AccessDeniedBusinessException.class, () -> createUserUseCase.execute(userData));
    }

}
