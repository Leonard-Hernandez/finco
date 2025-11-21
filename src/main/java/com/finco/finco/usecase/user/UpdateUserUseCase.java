package com.finco.finco.usecase.user;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserUpdateData;

public class UpdateUserUseCase {

    private final UserGateway userGateway;
    private final AuthGateway authGateway;

    public UpdateUserUseCase(UserGateway userGateway, AuthGateway authGateway) {
        this.userGateway = userGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    @LogExecution(logReturnValue = false, logArguments = false)
    public User execute(Long id, IUserUpdateData userData) {

        authGateway.verifyOwnershipOrAdmin(id);
        
        User user = userGateway.findById(id).orElseThrow(() -> new UserNotFoundException());

        if (userData.name() != null && !userData.name().isEmpty() && !user.getName().equals(userData.name())) {
            user.setName(userData.name());            
        }
        if (userData.enable() != null && !user.isEnable().equals(userData.enable())) {
            user.setEnable(userData.enable());
        }
        if (userData.defaultCurrency() != null && !user.getDefaultCurrency().equals(userData.defaultCurrency())) {
            user.setDefaultCurrency(userData.defaultCurrency());
        }

        return userGateway.update(user);
    }

}
