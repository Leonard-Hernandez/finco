package com.finco.finco.entity.goal.gateway;

import java.util.Optional;

import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;

public interface GoalGateway {

    Goal create(Goal goal);
    Goal update(Goal goal);
    Goal delete(Goal goal);

    Optional<Goal> findById(Long id);

    PagedResult<Goal> findAllByUserId(Long userId, PageRequest pageRequest);

}
