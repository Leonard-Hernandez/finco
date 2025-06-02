package com.finco.finco.usecase.user;

import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserUpdateData;

public class UpdateUserUseCase {

    private final UserGateway userGateway;

    public UpdateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(IUserUpdateData userData) {
        User user = new User();
        user.setId(userData.id());
        user.setName(userData.name());
        user.setEnable(userData.enable());

        return userGateway.update(user);
    }

}
