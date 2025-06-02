package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.stereotype.Component;

import com.finco.finco.entity.role.model.Role;
import com.finco.finco.infrastructure.config.db.schema.RoleSchema;

@Component
public class RoleMapper {

    public Role toRole(RoleSchema roleSchema) {
        if (roleSchema == null) {
            return null;
        }

        Role role = new Role();

        role.setId(roleSchema.getId());
        role.setName(roleSchema.getName());

        return role;
    }

    public RoleSchema toRoleSchema(Role role) {
        if (role == null) {
            return null;
        }

        RoleSchema roleSchema = new RoleSchema();

        roleSchema.setId(role.getId());
        roleSchema.setName(role.getName());

        return roleSchema;
    }

}
