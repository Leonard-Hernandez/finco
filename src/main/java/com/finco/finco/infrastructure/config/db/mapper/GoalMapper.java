package com.finco.finco.infrastructure.config.db.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.db.schema.AccountSchema;
import com.finco.finco.infrastructure.config.db.schema.GoalAccountBalanceSchema;
import com.finco.finco.infrastructure.config.db.schema.GoalSchema;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Component
public class GoalMapper {

    private final UserMapper userMapper;

    public GoalMapper(@Lazy UserMapper usermMapper) {
        this.userMapper = usermMapper;
    }

    public Goal toGoal(GoalSchema goalSchema) {
        if (goalSchema == null) {
            return null;
        }

        Goal goal = new Goal();

        goal.setId(goalSchema.getId());
        goal.setUser(userMapper.toLigthUser(goalSchema.getUser()));
        goal.setName(goalSchema.getName());
        goal.setTargetAmount(goalSchema.getTargetAmount());
        goal.setDeadLine(goalSchema.getDeadLine());
        goal.setDescription(goalSchema.getDescription());
        goal.setCreationDate(goalSchema.getCreationDate());
        goal.setEnable(goalSchema.isEnable());
        goal.setGoalAccountBalances(toGoalAccountBalanceList(goalSchema.getGoalAccountBalances()));

        return goal;

    }

    public Goal toGoalWithoutUser(GoalSchema goalSchema) {
        if (goalSchema == null) {
            return null;
        }

        Goal goal = new Goal();

        goal.setId(goalSchema.getId());
        goal.setUser(null);
        goal.setName(goalSchema.getName());
        goal.setTargetAmount(goalSchema.getTargetAmount());
        goal.setDeadLine(goalSchema.getDeadLine());
        goal.setDescription(goalSchema.getDescription());
        goal.setCreationDate(goalSchema.getCreationDate());
        goal.setEnable(goalSchema.isEnable());
        goal.setGoalAccountBalances(toGoalAccountBalanceList(goalSchema.getGoalAccountBalances()));

        return goal;

    }

    public GoalSchema toGoalSchema(Goal goal) {

        if (goal == null) {
            return null;
        }

        GoalSchema goalSchema = new GoalSchema();

        if (goal.getUser() == null || goal.getUser().getId() == null) {
            throw new IllegalArgumentException("Goal must be associated with an existing User (with an ID).");
        }

        goalSchema.setId(goal.getId());
        UserSchema userSchema = new UserSchema();
        userSchema.setId(goal.getUser().getId());
        goalSchema.setUser(userSchema);
        goalSchema.setName(goal.getName());
        goalSchema.setTargetAmount(goal.getTargetAmount());
        goalSchema.setDeadLine(goal.getDeadLine());
        goalSchema.setDescription(goal.getDescription());
        goalSchema.setCreationDate(goal.getCreationDate());
        goalSchema.setEnable(goal.isEnable());
        if (goal.getGoalAccountBalances() != null) {
            goalSchema.setGoalAccountBalances(toGoalAccountBalanceSchemaList(goal.getGoalAccountBalances()));
        } else {
            goalSchema.setGoalAccountBalances(List.of());
        }

        return goalSchema;
    }

    public PagedResult<Goal> toGoalPagedResult(Page<GoalSchema> goalSchemaPage, PageRequest pageRequest) {
        if (goalSchemaPage == null) {
            return PagedResult.empty(pageRequest);
        }

        List<Goal> goalList = goalSchemaPage.getContent().stream()
                                            .map(this::toGoal)
                                            .collect(Collectors.toList());

        return new PagedResult<>(
            goalList,
            goalSchemaPage.getTotalElements(),
            goalSchemaPage.getTotalPages(),
            goalSchemaPage.getNumber(),
            goalSchemaPage.getSize(),
            goalSchemaPage.isFirst(),
            goalSchemaPage.isLast(),
            goalSchemaPage.hasNext(),
            goalSchemaPage.hasPrevious()
        );
    }

    public List<GoalAccountBalance> toGoalAccountBalanceList(List<GoalAccountBalanceSchema> goalAccountBalanceSchemaList) {
        if (goalAccountBalanceSchemaList == null) {
            return null;
        }
        return goalAccountBalanceSchemaList.stream()
                .map(goalAccountBalanceSchema -> {
                    GoalAccountBalance goalAccountBalance = new GoalAccountBalance();
                    goalAccountBalance.setId(goalAccountBalanceSchema.getId());
                    Goal goal = new Goal();
                    goal.setId(goalAccountBalanceSchema.getGoal().getId());
                    goalAccountBalance.setGoal(goal);
                    Account account = new Account();
                    account.setId(goalAccountBalanceSchema.getAccount().getId());
                    goalAccountBalance.setAccount(account);
                    goalAccountBalance.setBalance(goalAccountBalanceSchema.getBalance());
                    goalAccountBalance.setLastUpdated(goalAccountBalanceSchema.getLastUpdated());
                    goalAccountBalance.setCreatedAt(goalAccountBalanceSchema.getCreatedAt());
                    return goalAccountBalance;
                })
                .collect(Collectors.toList());
    }

    public List<GoalAccountBalanceSchema> toGoalAccountBalanceSchemaList(List<GoalAccountBalance> goalAccountBalanceList) {
        if (goalAccountBalanceList == null) {
            return null;
        }
        return goalAccountBalanceList.stream()
                .map(goalAccountBalance -> {
                    GoalAccountBalanceSchema goalAccountBalanceSchema = new GoalAccountBalanceSchema();
                    goalAccountBalanceSchema.setId(goalAccountBalance.getId());
                    GoalSchema goalSchema = new GoalSchema();
                    goalSchema.setId(goalAccountBalance.getGoal().getId());
                    goalAccountBalanceSchema.setGoal(goalSchema);
                    AccountSchema accountSchema = new AccountSchema();
                    accountSchema.setId(goalAccountBalance.getAccount().getId());
                    goalAccountBalanceSchema.setAccount(accountSchema);
                    goalAccountBalanceSchema.setBalance(goalAccountBalance.getBalance());
                    goalAccountBalanceSchema.setLastUpdated(goalAccountBalance.getLastUpdated());
                    goalAccountBalanceSchema.setCreatedAt(goalAccountBalance.getCreatedAt());
                    return goalAccountBalanceSchema;
                })
                .collect(Collectors.toList());
    }

}
