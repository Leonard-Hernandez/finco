package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.finco.finco.usecase.goal.dto.IGoalUpdateData;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Goal update data")
public record GoalUpdateData(
        @NotNull(message = "Name is required")
        @Schema(description = "Goal name")
        String name,

        @NotNull(message = "Target amount is required")
        @Min(value = 0, message = "Target amount must be positive")
        @Schema(description = "Goal target amount")
        BigDecimal targetAmount,

        @NotNull(message = "Dead line is required")
        @Schema(description = "Goal dead line")
        LocalDate deadLine,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        @Schema(description = "Goal description")
        String description,
        
        @Schema(description = "Goal enable")
        Boolean enable) implements IGoalUpdateData {

}
