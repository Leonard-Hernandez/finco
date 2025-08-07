package com.finco.finco.infrastructure.config.db.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Repository
public interface UserRepository extends JpaRepository<UserSchema, Long>, JpaSpecificationExecutor<UserSchema> {

    Optional<UserSchema> findByEmail(String email);

    Page<UserSchema> findAllByEnableTrue(Pageable pageable);

    @NonNull
    @Override
    Page<UserSchema> findAll(@Nullable Specification<UserSchema> specification, @Nullable Pageable pageable);

}
