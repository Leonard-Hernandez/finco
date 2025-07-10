package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.finco.finco.usecase.goal.dto.IGoalRegistrationData;

public record GoalRegistrationData(String name, BigDecimal targetAmount, LocalDate deadLine, String description) implements IGoalRegistrationData{

}
