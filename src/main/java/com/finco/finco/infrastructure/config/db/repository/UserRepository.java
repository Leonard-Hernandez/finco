package com.finco.finco.infrastructure.config.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finco.finco.infrastructure.config.db.schema.UserSchema;

public interface UserRepository extends JpaRepository<UserSchema, Long> {

}
