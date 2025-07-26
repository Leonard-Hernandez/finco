package com.finco.finco.usecase.user;

import java.time.LocalDateTime;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.role.exception.RoleNotFoundException;
import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserRegistrationData;
import java.util.List;

public class CreateUserAdminUseCase {

    private final UserGateway userGateway;
    private final RoleGateway roleGateway;
    private final AuthGateway authGateway;

    public CreateUserAdminUseCase(UserGateway userGateway, RoleGateway roleGateway, AuthGateway authGateway) {
        this.userGateway = userGateway;
        this.roleGateway = roleGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation()
    @LogExecution(logReturnValue = false, logArguments = false)
    public User execute(IUserRegistrationData userData) {
        if (!authGateway.isAuthenticatedUserInRole("ADMIN")) {
            throw new AccessDeniedBusinessException();
        }

        User user = new User();
        user.setName(userData.name());
        user.setEmail(userData.email());
        user.setPassword(userData.password());
        user.setEnable(true);
        user.setRegistrationDate(LocalDateTime.now());

        List<Role> roles = List.of(
            roleGateway.findByName("ROLE_USER").orElseThrow(() -> new RoleNotFoundException()),
            roleGateway.findByName("ROLE_ADMIN").orElseThrow(() -> new RoleNotFoundException())
        );

        user.setRoles(roles);

        return userGateway.create(user);
    }

}
