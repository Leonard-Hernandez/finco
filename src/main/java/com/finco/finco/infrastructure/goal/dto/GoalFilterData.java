package com.finco.finco.infrastructure.goal.dto;

import com.finco.finco.entity.pagination.filter.IGoalFilterData;

public record GoalFilterData(Long userId, Long accountId) implements IGoalFilterData{

}
