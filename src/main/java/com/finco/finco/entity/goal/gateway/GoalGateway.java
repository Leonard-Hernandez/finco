package com.finco.finco.entity.goal.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.user.model.User;

public interface GoalGateway {

    Goal create(Goal goal);
    Goal Update(Goal goal);
    void delete(Goal goal);

    Optional<Goal> findById(Long id);

    List<Goal> findAllByUser(User user);

    List<Goal> findAllByUserAndNameLike(User user, String search);

}
