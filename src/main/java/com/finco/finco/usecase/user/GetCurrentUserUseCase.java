package com.finco.finco.usecase.user;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;

public class GetCurrentUserUseCase {

    private final UserGateway userGateway;
    private final AuthGateway authGateway;

    public GetCurrentUserUseCase(UserGateway userGateway, AuthGateway authGateway) {
        this.userGateway = userGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public User execute() {
        Long id = authGateway.getAuthenticatedUserId();
        return userGateway.findById(id).orElseThrow(UserNotFoundException::new);
    }

}
