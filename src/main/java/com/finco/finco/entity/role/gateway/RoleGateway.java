package com.finco.finco.entity.role.gateway;

import java.util.Optional;

import com.finco.finco.entity.role.model.Role;

public interface RoleGateway {

    Optional<Role> findByName(String name);

}
