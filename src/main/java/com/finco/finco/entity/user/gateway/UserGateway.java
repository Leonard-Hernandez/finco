package com.finco.finco.entity.user.gateway;

import java.util.Optional;

import com.finco.finco.entity.user.model.User;

public interface UserGateway {

    void delete(User user);
    void create(User user);
    void update(User user);

    Optional<User> findById(Long id);

}
