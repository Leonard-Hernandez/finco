package com.finco.finco.infrastructure.config.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finco.finco.infrastructure.config.db.schema.GoalSchema;

@Repository
public interface GoalRepository extends JpaRepository<GoalSchema, Long> {

    List<GoalSchema> findByUserId(Long id);

    List<GoalSchema> findByUserIdAndNameLike(Long userId, String search);

    Page<GoalSchema> findAllByUserId(Pageable pageable, Long userId);

}
