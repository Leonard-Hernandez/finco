package com.finco.finco.infrastructure.config.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

import com.finco.finco.infrastructure.config.db.schema.GoalAccountBalanceSchema;

public interface GoalAccountBalanceRepository extends JpaRepository<GoalAccountBalanceSchema, Long>{

    List<GoalAccountBalanceSchema> findAllByGoalId(Long goalId);

    List<GoalAccountBalanceSchema> findAllByAccountId(Long accountId);

    @Query("SELECT SUM(gab.balance) as total FROM GoalAccountBalanceSchema gab WHERE gab.account.id = :accountId")
    BigDecimal getTotalBalanceInGoalsByAccount(Long accountId);

}
