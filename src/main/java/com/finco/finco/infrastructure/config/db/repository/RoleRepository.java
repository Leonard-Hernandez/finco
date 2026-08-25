package com.finco.finco.infrastructure.config.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finco.finco.infrastructure.config.db.schema.RoleSchema;

public interface RoleRepository extends JpaRepository<RoleSchema, Long> {

        Optional<RoleSchema> findByName(String name);

}
