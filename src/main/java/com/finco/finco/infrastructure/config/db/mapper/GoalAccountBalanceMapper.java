package com.finco.finco.infrastructure.config.db.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.infrastructure.config.db.schema.GoalAccountBalanceSchema;

@Component
public class GoalAccountBalanceMapper {

    private final GoalMapper goalMapper;
    private final AccountMapper accountMapper;

    public GoalAccountBalanceMapper(@Lazy GoalMapper goalMapper, AccountMapper accountMapper) {
        this.goalMapper = goalMapper;
        this.accountMapper = accountMapper;
    }

    public GoalAccountBalance toGoalAccountBalance(GoalAccountBalanceSchema goalAccountBalanceSchema) {
        if (goalAccountBalanceSchema == null) {
            return null;
        }

        GoalAccountBalance goalAccountBalance = new GoalAccountBalance();

        goalAccountBalance.setId(goalAccountBalanceSchema.getId());
        goalAccountBalance.setGoal(goalMapper.toLightGoal(goalAccountBalanceSchema.getGoal()));
        goalAccountBalance.setAccount(accountMapper.toAccount(goalAccountBalanceSchema.getAccount()));
        goalAccountBalance.setBalance(goalAccountBalanceSchema.getBalance());
        goalAccountBalance.setLastUpdated(goalAccountBalanceSchema.getLastUpdated());
        goalAccountBalance.setCreatedAt(goalAccountBalanceSchema.getCreatedAt());   

        return goalAccountBalance;
    }

    public GoalAccountBalanceSchema toGoalAccountBalanceSchema(GoalAccountBalance goalAccountBalance) {
        if (goalAccountBalance == null) {
            return null;
        }

        GoalAccountBalanceSchema goalAccountBalanceSchema = new GoalAccountBalanceSchema();

        goalAccountBalanceSchema.setId(goalAccountBalance.getId());
        goalAccountBalanceSchema.setGoal(goalMapper.toLightGoalSchema(goalAccountBalance.getGoal()));
        goalAccountBalanceSchema.setAccount(accountMapper.toAccountSchema(goalAccountBalance.getAccount()));
        goalAccountBalanceSchema.setBalance(goalAccountBalance.getBalance());
        goalAccountBalanceSchema.setLastUpdated(goalAccountBalance.getLastUpdated());
        goalAccountBalanceSchema.setCreatedAt(goalAccountBalance.getCreatedAt());   

        return goalAccountBalanceSchema;
    }

}
