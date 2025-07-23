package com.finco.finco.usecase.user;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;

public class GetAllUserUseCase {

    private final UserGateway userGateway;
    private final AuthGateway authGateway;

    public GetAllUserUseCase(UserGateway userGateway, AuthGateway authGateway) {
        this.userGateway = userGateway;
        this.authGateway = authGateway;
    }


    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false) // Avoid logging potentially large user lists
    public PagedResult<User> execute(PageRequest page) {

        if (!authGateway.isAuthenticatedUserInRole("ADMIN")) {
            throw new AccessDeniedBusinessException();
        }

        return userGateway.findAll(page);

    }

}
