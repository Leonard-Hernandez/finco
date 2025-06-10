package com.finco.finco.usecase.user;

import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;

public class DeleteUserUseCase {

    private UserGateway userGateway;

    public DeleteUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User excute(Long id) {
        User user = userGateway.findById(id).orElseThrow(UserNotFoundException::new);

        userGateway.delete(user);

        return user;

    }

}
