package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.finco.finco.usecase.goal.dto.IGoalRegistrationData;

import jakarta.validation.constraints.NotNull;

public record GoalRegistrationData(
    @NotNull(message = "Name is required")
    String name, 
    @NotNull(message = "Target amount is required")
    BigDecimal targetAmount, 
    @NotNull(message = "Dead line is required")
    LocalDate deadLine, 
    String description)
        implements IGoalRegistrationData {

}
