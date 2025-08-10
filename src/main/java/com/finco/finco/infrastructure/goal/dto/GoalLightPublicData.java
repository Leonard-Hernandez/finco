package com.finco.finco.infrastructure.goal.dto;

import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.usecase.goal.dto.IGoalLightPublicData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Goal light public data")
public record GoalLightPublicData(
    @Schema(description = "Goal id")
    Long id,
    @Schema(description = "Goal name")
    String name,
    @Schema(description = "Goal description")
    String description
) implements IGoalLightPublicData {

    public GoalLightPublicData(Goal goal) {
        this(goal.getId(), goal.getName(), goal.getDescription());
    }

}