package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;

import com.finco.finco.usecase.goal.dto.IGoalTransactionData;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GoalTransactionData(
    @NotNull(message = "Account ID is required")
    Long accountId, 
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    BigDecimal amount, 
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description, 
    String category)
                implements IGoalTransactionData {

}
