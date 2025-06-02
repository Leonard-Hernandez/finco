package com.finco.finco.usecase.user;

import java.time.LocalDateTime;

import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserRegistrationData;

public class CreateUserUserCase {

    private final UserGateway userGateway;

    public CreateUserUserCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(IUserRegistrationData userData) {
        User user = new User();
        user.setName(userData.name());
        user.setEmail(userData.name());
        user.setPassword(userData.password());
        user.setEnable(true);
        user.setRegistrationDate(LocalDateTime.now());

        return userGateway.create(user);
    }

}
