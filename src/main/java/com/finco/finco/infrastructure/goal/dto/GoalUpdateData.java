package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.finco.finco.usecase.goal.dto.IGoalUpdateData;

public record GoalUpdateData(String name, BigDecimal targetAmount, LocalDate deadLine, String description,
        Boolean enable) implements IGoalUpdateData {

}
