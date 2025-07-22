package com.finco.finco.infrastructure.goalaccountbalance.gateway;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.finco.finco.entity.goalAccountBalance.gateway.GoalAccountBalanceGateway;
import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.infrastructure.config.db.mapper.GoalAccountBalanceMapper;
import com.finco.finco.infrastructure.config.db.repository.GoalAccountBalanceRepository;

@Component
public class GoalAccountBalanceDatabaseGateway implements GoalAccountBalanceGateway {

    private final GoalAccountBalanceRepository goalAccountBalanceRepository;
    private final GoalAccountBalanceMapper goalAccountBalanceMapper;

    public GoalAccountBalanceDatabaseGateway(GoalAccountBalanceRepository goalAccountBalanceRepository,
            GoalAccountBalanceMapper goalAccountBalanceMapper) {
        this.goalAccountBalanceRepository = goalAccountBalanceRepository;
        this.goalAccountBalanceMapper = goalAccountBalanceMapper;
    }

    @Override
    public GoalAccountBalance create(GoalAccountBalance goalAccountBalance) {
        return goalAccountBalanceMapper.toGoalAccountBalance(goalAccountBalanceRepository
                .save(goalAccountBalanceMapper.toGoalAccountBalanceSchema(goalAccountBalance)));
    }

    @Override
    public GoalAccountBalance update(GoalAccountBalance goalAccountBalance) {
        return goalAccountBalanceMapper.toGoalAccountBalance(goalAccountBalanceRepository
                .save(goalAccountBalanceMapper.toGoalAccountBalanceSchema(goalAccountBalance)));
    }

    @Override
    public GoalAccountBalance findById(Long id) {
        return goalAccountBalanceMapper.toGoalAccountBalance(goalAccountBalanceRepository.findById(id).orElse(null));
    }

    @Override
    public List<GoalAccountBalance> findAllByGoalId(Long goalId) {
        return goalAccountBalanceRepository.findAllByGoalId(goalId).stream()
                .map(goalAccountBalanceMapper::toGoalAccountBalance).toList();
    }

    @Override
    public List<GoalAccountBalance> findAllByAccountId(Long accountId) {
        return goalAccountBalanceRepository.findAllByAccountId(accountId).stream()
                .map(goalAccountBalanceMapper::toGoalAccountBalance).toList();
    }

    @Override
    public BigDecimal getTotalBalanceInGoalsByAccount(Long accountId) {
        BigDecimal totalBalanceInGoalsByAccount = goalAccountBalanceRepository.getTotalBalanceInGoalsByAccount(accountId);
        return totalBalanceInGoalsByAccount != null ? totalBalanceInGoalsByAccount : BigDecimal.ZERO;
    }

}
