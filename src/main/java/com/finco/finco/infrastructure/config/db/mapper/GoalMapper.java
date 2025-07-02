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
        goal.setSavedAmount(goalSchema.getSavedAmount());

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
        goal.setSavedAmount(goalSchema.getSavedAmount());

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
        goalSchema.setUser(userMapper.toUserSchema(goal.getUser()));
        goalSchema.setName(goal.getName());
        goalSchema.setTargetAmount(goal.getTargetAmount());
        goalSchema.setDeadLine(goal.getDeadLine());
        goalSchema.setDescription(goal.getDescription());
        goalSchema.setCreationDate(goal.getCreationDate());
        goalSchema.setSavedAmount(goal.getSavedAmount());

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

}
