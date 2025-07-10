package com.finco.finco.infrastructure.goalaccountbalance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.usecase.goalaccountbalance.dto.IGoalAccountBalanceData;

public record GoalAccountBalanceData(Long id, Long goalId, Long accountId, BigDecimal balance,
        LocalDateTime lastUpdated, LocalDateTime createdAt) implements IGoalAccountBalanceData {

    public GoalAccountBalanceData(GoalAccountBalance goalAccountBalance) {
        this(goalAccountBalance.getId(), goalAccountBalance.getGoal().getId(), goalAccountBalance.getAccount().getId(),
                goalAccountBalance.getBalance(), goalAccountBalance.getLastUpdated(),
                goalAccountBalance.getCreatedAt());
    }
}
