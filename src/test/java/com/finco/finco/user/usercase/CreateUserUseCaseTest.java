package com.finco.finco.user.usercase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.role.exception.RoleNotFoundException;
import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.CreateUserUseCase;
import com.finco.finco.usecase.user.dto.IUserRegistrationData;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private RoleGateway roleGateway;

    private CreateUserUseCase createUserUseCase;
    private IUserRegistrationData userData;
    private Role role;

    @BeforeEach
    public void setUp() {
        createUserUseCase = new CreateUserUseCase(userGateway, roleGateway);
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
            public CurrencyEnum defaultCurrency() {
                return CurrencyEnum.USD;
            }

            @Override
            public String password() {
                return "123";
            }
        };
        role = new Role();
        role.setName("ROLE_USER");
    }

    @Test
    @DisplayName("Create user success")
    public void createUserSuccess() {
        // Arrange
        when(roleGateway.findByName("ROLE_USER")).thenReturn(Optional.of(role));
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

        user.setRoles(List.of(role));

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
    @DisplayName("Create user role not found")
    public void createUserRoleNotFound() {
        // Arrange
        when(roleGateway.findByName("ROLE_USER")).thenReturn(Optional.empty());

        // Act and Asset
        assertThrows(RoleNotFoundException.class, () -> createUserUseCase.execute(userData));
    }

}
