package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;

import com.finco.finco.usecase.goal.dto.IGoalDepositData;

public record GoalDepositData(Long accountId, BigDecimal amount, String description, String category)
        implements IGoalDepositData {

}
