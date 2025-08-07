package com.finco.finco.entity.goal.gateway;

import java.util.Optional;

import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.pagination.filter.IGoalFilterData;

public interface GoalGateway {

    Goal create(Goal goal);
    Goal update(Goal goal);
    Goal delete(Goal goal);

    Optional<Goal> findById(Long id);

    PagedResult<Goal> findAll(PageRequest pageRequest);

    PagedResult<Goal> findAllByFilterData(IGoalFilterData filterData, PageRequest pageRequest);

    PagedResult<Goal> findAllByUserId(Long userId, PageRequest pageRequest);

}
