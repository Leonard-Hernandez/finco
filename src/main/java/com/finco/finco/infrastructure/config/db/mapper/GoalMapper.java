package com.finco.finco.infrastructure.config.db.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.db.schema.GoalSchema;

@Component
public class GoalMapper {

    private final UserMapper userMapper;
    private final GoalAccountBalanceMapper goalAccountBalanceMapper;

    public GoalMapper(@Lazy UserMapper usermMapper, GoalAccountBalanceMapper goalAccountBalanceMapper) {
        this.userMapper = usermMapper;
        this.goalAccountBalanceMapper = goalAccountBalanceMapper;
    }

    public Goal toGoal(GoalSchema goalSchema) {
        if (goalSchema == null) {
            return null;
        }

        Goal goal = new Goal();

        goal.setId(goalSchema.getId());
        goal.setUser(userMapper.toLightUser(goalSchema.getUser()));
        goal.setName(goalSchema.getName());
        goal.setTargetAmount(goalSchema.getTargetAmount());
        goal.setDeadLine(goalSchema.getDeadLine());
        goal.setDescription(goalSchema.getDescription());
        goal.setCreationDate(goalSchema.getCreationDate());
        goal.setEnable(goalSchema.isEnable());
        if (goalSchema.getGoalAccountBalances() != null) {
            goal.setGoalAccountBalances(goalSchema.getGoalAccountBalances().stream()
                    .map(goalAccountBalanceMapper::toGoalAccountBalance).collect(Collectors.toList()));
        } else {
            goal.setGoalAccountBalances(List.of());
        }

        return goal;

    }

    public Goal toLightGoal(GoalSchema goalSchema) {
        if (goalSchema == null) {
            return null;
        }

        Goal goal = new Goal();

        goal.setId(goalSchema.getId());
        goal.setUser(userMapper.toLightUser(goalSchema.getUser()));
        goal.setName(goalSchema.getName());
        goal.setTargetAmount(goalSchema.getTargetAmount());
        goal.setDeadLine(goalSchema.getDeadLine());
        goal.setDescription(goalSchema.getDescription());
        goal.setCreationDate(goalSchema.getCreationDate());
        goal.setEnable(goalSchema.isEnable());

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
        goalSchema.setUser(userMapper.toLightUserSchema(goal.getUser()));
        goalSchema.setName(goal.getName());
        goalSchema.setTargetAmount(goal.getTargetAmount());
        goalSchema.setDeadLine(goal.getDeadLine());
        goalSchema.setDescription(goal.getDescription());
        goalSchema.setCreationDate(goal.getCreationDate());
        goalSchema.setEnable(goal.isEnable());
        if (goal.getGoalAccountBalances() != null) {
            goalSchema.setGoalAccountBalances(goal.getGoalAccountBalances().stream()
                    .map(goalAccountBalanceMapper::toGoalAccountBalanceSchema).collect(Collectors.toList()));
        } else {
            goalSchema.setGoalAccountBalances(List.of());
        }

        return goalSchema;
    }

    public GoalSchema toLightGoalSchema(Goal goal) {
        if (goal == null) {
            return null;
        }

        GoalSchema goalSchema = new GoalSchema();

        goalSchema.setId(goal.getId());
        goalSchema.setUser(userMapper.toLightUserSchema(goal.getUser()));
        goalSchema.setName(goal.getName());
        goalSchema.setTargetAmount(goal.getTargetAmount());
        goalSchema.setDeadLine(goal.getDeadLine());
        goalSchema.setDescription(goal.getDescription());
        goalSchema.setCreationDate(goal.getCreationDate());
        goalSchema.setEnable(goal.isEnable());

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
                goalSchemaPage.hasPrevious());
    }

}
