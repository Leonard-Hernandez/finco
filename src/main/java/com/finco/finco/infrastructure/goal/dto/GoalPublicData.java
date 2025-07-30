package com.finco.finco.infrastructure.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.infrastructure.goalaccountbalance.dto.GoalAccountBalanceData;
import com.finco.finco.usecase.goal.dto.IGoalPublicData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Goal public data")
public record GoalPublicData(
    @Schema(description = "Goal id")
    Long id,
    @Schema(description = "Goal name")
    String name,
    @Schema(description = "Goal target amount")
    BigDecimal targetAmount,
    @Schema(description = "Goal dead line")
    LocalDate deadLine,
    @Schema(description = "Goal description")
    String description,
    @Schema(description = "Goal creation date")
    LocalDateTime creationDate,
    @Schema(description = "Goal enable")
    boolean enable,
    @Schema(description = "Goal account balances")
    List<GoalAccountBalanceData> goalAccountBalances
) implements IGoalPublicData {

    public GoalPublicData(Goal goal) {
        this(goal.getId(), goal.getName(), goal.getTargetAmount(), goal.getDeadLine(), goal.getDescription(),
                goal.getCreationDate(), goal.isEnable(), goal.getGoalAccountBalances().stream()
                .map(goalAccountBalance -> new GoalAccountBalanceData(goalAccountBalance))
                .collect(Collectors.toList()));
    }

}
