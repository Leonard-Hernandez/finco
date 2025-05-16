package com.finco.finco.entity.goalallocation.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.goalallocation.model.GoalAllocation;

public interface GoalAllocationGateway {

    GoalAllocation create(GoalAllocation goalAllocation);
    GoalAllocation update(GoalAllocation goalAllocation);
    void delete(GoalAllocation goalAllocation);

    Optional<GoalAllocation> findById(Long id);

    List<GoalAllocation> findByGoalId(Long goalId);

    List<GoalAllocation> findbyUserId(Long userId);

    List<GoalAllocation> findByAccountId(Long accountId);

}
