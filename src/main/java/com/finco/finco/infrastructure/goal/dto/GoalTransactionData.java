package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;

import com.finco.finco.usecase.goal.dto.IGoalTransactionData;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Goal transaction data")
public record GoalTransactionData(
    @NotNull(message = "Account ID is required")
    @Schema(description = "Account ID", example = "1")
    Long accountId, 

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    @Schema(description = "Amount", example = "100")
    BigDecimal amount, 

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Description", example = "Description")
    String description, 
    
    @Schema(description = "Category", example = "Category")
    String category)
                implements IGoalTransactionData {

}
