package com.finco.finco.entity.user.gateway;

import java.util.Optional;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.user.model.User;

public interface UserGateway {

    User create(User user);
    User update(User user);
    void delete(User user);

    Optional<User> findById(Long id);

    PagedResult<User> findAll(PageRequest pageRequest);

}
