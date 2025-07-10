package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.finco.finco.usecase.goalaccountbalance.dto.IGoalAccountBalanceData;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.infrastructure.goalaccountbalance.dto.GoalAccountBalanceData;
import com.finco.finco.usecase.goal.dto.IGoalPublicData;

public record GoalPublicData(
    Long id,
    String name,
    BigDecimal targetAmount,
    LocalDate deadLine,
    String description,
    LocalDateTime creationDate,
    boolean enable,
    List<IGoalAccountBalanceData> goalAccountBalances
) implements IGoalPublicData {

    public GoalPublicData(Goal goal) {
        this(goal.getId(), goal.getName(), goal.getTargetAmount(), goal.getDeadLine(), goal.getDescription(),
                goal.getCreationDate(), goal.isEnable(), goal.getGoalAccountBalances().stream()
                .map(goalAccountBalance -> new GoalAccountBalanceData(goalAccountBalance))
                .collect(Collectors.toList()));
    }

}
