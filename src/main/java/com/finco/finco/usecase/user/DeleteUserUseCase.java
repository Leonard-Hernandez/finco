package com.finco.finco.usecase.user;

import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;

public class DeleteUserUseCase {

    private final UserGateway userGateway;
    private final AuthGateway authGateway;

    public DeleteUserUseCase(UserGateway userGateway, AuthGateway authGateway) {
        this.userGateway = userGateway;
        this.authGateway = authGateway;
    }

    public User excute(Long id) {
        authGateway.verifyOwnershipOrAdmin(id);

        User user = userGateway.findById(id).orElseThrow(UserNotFoundException::new);

        userGateway.delete(user);

        return user;

    }

}
