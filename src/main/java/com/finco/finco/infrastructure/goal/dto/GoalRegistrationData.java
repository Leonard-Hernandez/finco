package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.finco.finco.usecase.goal.dto.IGoalRegistrationData;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Goal registration data")
public record GoalRegistrationData(
    @Schema(description = "Goal name")
    @NotNull(message = "Name is required")
    String name, 
    @Schema(description = "Goal target amount")
    @NotNull(message = "Target amount is required")
    BigDecimal targetAmount, 
    @Schema(description = "Goal dead line")
    @NotNull(message = "Dead line is required")
    LocalDate deadLine, 
    String description)
        implements IGoalRegistrationData {

}
