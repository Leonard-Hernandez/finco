package com.finco.finco.infrastructure.goalaccountbalance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.infrastructure.account.dto.AccountLightPublicData;
import com.finco.finco.usecase.goalaccountbalance.dto.IGoalAccountBalanceData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Goal account balance data")
public record GoalAccountBalanceData(
        @Schema(description = "Goal account balance id") Long id,
        @Schema(description = "Goal id") Long goalId,
        @Schema(description = "Account id") AccountLightPublicData account,
        @Schema(description = "Goal account balance") BigDecimal balance,
        @Schema(description = "Goal account balance last updated") LocalDateTime lastUpdated,
        @Schema(description = "Goal account balance created at") LocalDateTime createdAt)
        implements IGoalAccountBalanceData {

    public GoalAccountBalanceData(GoalAccountBalance goalAccountBalance) {
        this(goalAccountBalance.getId(), goalAccountBalance.getGoal().getId(),
                new AccountLightPublicData(goalAccountBalance.getAccount()),
                goalAccountBalance.getBalance(), goalAccountBalance.getLastUpdated(),
                goalAccountBalance.getCreatedAt());
    }
}
