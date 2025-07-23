package com.finco.finco.usecase.user;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
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

    @TransactionalDomainAnnotation()
    @LogExecution
    public User execute(Long userId) {
        authGateway.verifyOwnershipOrAdmin(userId);

        User user = userGateway.findById(userId).orElseThrow(UserNotFoundException::new);

        return userGateway.delete(user);
    }

}
