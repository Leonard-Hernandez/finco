package com.finco.finco.usecase.user;

import java.time.LocalDateTime;

import com.finco.finco.entity.role.exception.RoleNotFoundException;
import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserRegistrationData;
import java.util.List;

public class CreateUserAdminUseCase {

    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    public CreateUserAdminUseCase(UserGateway userGateway, RoleGateway roleGateway) {
        this.userGateway = userGateway;
        this.roleGateway = roleGateway;
    }

    public User execute(IUserRegistrationData userData) {
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
