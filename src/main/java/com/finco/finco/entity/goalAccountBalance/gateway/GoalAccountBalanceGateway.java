package com.finco.finco.entity.goalAccountBalance.gateway;

import java.util.List;
import java.util.Optional;

import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;

public interface GoalAccountBalanceGateway {

    GoalAccountBalance create(GoalAccountBalance goalAccountBalance);

    GoalAccountBalance update(GoalAccountBalance goalAccountBalance);

    GoalAccountBalance findById(Long id);

    List<GoalAccountBalance> findAllByGoalId(Long goalId);

    List<GoalAccountBalance> findAllByAccountId(Long accountId);

    Optional<GoalAccountBalance> findByGoalIdAndAccountId(Long goalId, Long accountId);


}
