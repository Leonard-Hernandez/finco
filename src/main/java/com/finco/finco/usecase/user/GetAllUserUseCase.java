package com.finco.finco.usecase.user;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;

public class GetAllUserUseCase {

    private UserGateway userGateway;

    public GetAllUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public PagedResult<User> execute(PageRequest page) {
        return userGateway.findAll(page);
    }

}
