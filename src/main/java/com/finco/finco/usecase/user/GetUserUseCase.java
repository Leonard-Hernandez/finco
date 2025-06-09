package com.finco.finco.usecase.user;

import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;

public class GetUserUseCase {

    private UserGateway userGateway;

    public GetUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(Long id) {
        return userGateway.findById(id).orElseThrow(UserNotFoundException::new);
    }

}
