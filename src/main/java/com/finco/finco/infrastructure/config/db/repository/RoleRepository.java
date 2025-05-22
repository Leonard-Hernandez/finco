package com.finco.finco.infrastructure.config.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.RoleSchema;

@Repository
public interface RoleRepository extends JpaRepository<RoleSchema, Long> {

        Optional<RoleSchema> findByName(String name);

}
