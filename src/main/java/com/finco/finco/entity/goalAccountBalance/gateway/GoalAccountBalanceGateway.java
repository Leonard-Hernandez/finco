package com.finco.finco.entity.goalAccountBalance.gateway;

import java.math.BigDecimal;
import java.util.List;

import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;

public interface GoalAccountBalanceGateway {

    GoalAccountBalance create(GoalAccountBalance goalAccountBalance);

    GoalAccountBalance update(GoalAccountBalance goalAccountBalance);

    GoalAccountBalance findById(Long id);

    List<GoalAccountBalance> findAllByGoalId(Long goalId);

    List<GoalAccountBalance> findAllByAccountId(Long accountId);

    BigDecimal getTotalBalanceInGoalsByAccount(Long accountId);

}
