package com.finco.finco.infrastructure.config.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.finco.finco.infrastructure.config.db.schema.GoalAccountBalanceSchema;

public interface GoalAccountBalanceRepository extends JpaRepository<GoalAccountBalanceSchema, Long>{

    List<GoalAccountBalanceSchema> findAllByGoalId(Long goalId);

    List<GoalAccountBalanceSchema> findAllByAccountId(Long accountId);

}
