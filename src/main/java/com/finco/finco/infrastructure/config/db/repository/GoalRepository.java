package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.GoalSchema;

@Repository
public interface GoalRepository extends JpaRepository<GoalSchema, Long>, JpaSpecificationExecutor<GoalSchema> {

    List<GoalSchema> findByUserId(Long id);

    List<GoalSchema> findByUserIdAndNameLike(Long userId, String search);

    Page<GoalSchema> findAllByUserIdAndEnableTrue(Pageable pageable, Long userId);

    @NonNull
    @Override
    Page<GoalSchema> findAll(@Nullable Specification<GoalSchema> specification, @Nullable Pageable pageable);

}
