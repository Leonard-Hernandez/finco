package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;

import com.finco.finco.usecase.goal.dto.IGoalTransactionData;

public record GoalTransactionData(Long accountId, BigDecimal amount, String description, String category)
                implements IGoalTransactionData {

}
