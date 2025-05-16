package com.finco.finco.entity.goal.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.goal.model.Goal;

public interface GoalGateway {

    Goal create(Goal goal);
    Goal Update(Goal goal);
    void delete(Goal goal);

    Optional<Goal> findById(Long id);

    List<Goal> findByUserId(Long id);

    List<Goal> findByUserAndIdNameLike(Long userId, String search);

}
