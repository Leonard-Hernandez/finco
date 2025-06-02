package com.finco.finco.usecase.user;

import java.time.LocalDateTime;

import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserRegistrationData;
import java.util.List;

public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    public CreateUserUseCase(UserGateway userGateway, RoleGateway roleGateway) {
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

        user.setRoles(List.of(roleGateway.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"))));

        return userGateway.create(user);
    }

}
