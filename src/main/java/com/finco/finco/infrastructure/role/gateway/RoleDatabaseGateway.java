package com.finco.finco.infrastructure.role.gateway;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.finco.finco.entity.role.gateway.RoleGateway;
import com.finco.finco.entity.role.model.Role;
import com.finco.finco.infrastructure.config.db.mapper.RoleMapper;
import com.finco.finco.infrastructure.config.db.repository.RoleRepository;
import com.finco.finco.infrastructure.config.db.schema.RoleSchema;

@Component
public class RoleDatabaseGateway implements RoleGateway{

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDatabaseGateway(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Optional<Role> findByName(String name) {
        RoleSchema role = roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
        return Optional.of(roleMapper.toRole(role));
    }

}
