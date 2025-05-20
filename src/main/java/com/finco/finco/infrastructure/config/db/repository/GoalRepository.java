package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finco.finco.infrastructure.config.db.schema.GoalSchema;

public interface GoalRepository extends JpaRepository<GoalSchema, Long> {

    List<GoalSchema> findByUserId(Long id);

    List<GoalSchema> findByUserIdAndNameLike(Long userId, String search);

}
