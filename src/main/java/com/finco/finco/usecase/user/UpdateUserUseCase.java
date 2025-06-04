package com.finco.finco.usecase.user;

import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserUpdateData;

public class UpdateUserUseCase {

    private final UserGateway userGateway;

    public UpdateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(Long id, IUserUpdateData userData) {
        
        User user = userGateway.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (userData.name() != null && !userData.name().isEmpty()) {
            user.setName(userData.name());            
        }
        if (userData.enable() != null) {
            user.setEnable(userData.enable());
        }

        return userGateway.update(user);
    }

}
