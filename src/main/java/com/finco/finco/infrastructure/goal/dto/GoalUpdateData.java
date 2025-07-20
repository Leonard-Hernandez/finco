package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.finco.finco.usecase.goal.dto.IGoalUpdateData;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GoalUpdateData(
        @NotNull(message = "Name is required")
        String name, 
        @NotNull(message = "Target amount is required")
        @Min(value = 0, message = "Target amount must be positive")
        BigDecimal targetAmount, 
        @NotNull(message = "Dead line is required")
        LocalDate deadLine, 
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        Boolean enable) implements IGoalUpdateData {

}
