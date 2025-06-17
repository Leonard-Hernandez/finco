package com.finco.finco.usecase.user;

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

    public User execute(Long id, IUserUpdateData userData) {

        authGateway.verifyOwnershipOrAdmin(id);
        
        User user = userGateway.findById(id).orElseThrow(() -> new UserNotFoundException());

        if (userData.name() != null && !userData.name().isEmpty()) {
            user.setName(userData.name());            
        }
        if (userData.enable() != null) {
            user.setEnable(userData.enable());
        }

        return userGateway.update(user);
    }

}
