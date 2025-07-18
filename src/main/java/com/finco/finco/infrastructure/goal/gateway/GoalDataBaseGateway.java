package com.finco.finco.infrastructure.goal.gateway;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.db.mapper.GoalMapper;
import com.finco.finco.infrastructure.config.db.repository.GoalRepository;
import com.finco.finco.infrastructure.config.db.schema.GoalSchema;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@Component
public class GoalDataBaseGateway implements GoalGateway {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;

    public GoalDataBaseGateway(GoalRepository goalRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
    }

    @Override
    public Goal create(Goal goal) {
        return goalMapper.toGoal(goalRepository.save(goalMapper.toGoalSchema(goal)));
    }

    @Override
    public Goal update(Goal goal) {
        return goalMapper.toGoal(goalRepository.save(goalMapper.toGoalSchema(goal)));
    }

    @Override
    public Goal delete(Goal goal) {
        goal.setEnable(false);
        return goalMapper.toGoal(goalRepository.save(goalMapper.toGoalSchema(goal)));
    }

    @Override
    public Optional<Goal> findById(Long id) {
        return goalRepository.findById(id).map(goalMapper::toGoal);
    }

    @Override
    public PagedResult<Goal> findAllByUserId(Long userId, PageRequest pageRequest) {

        Pageable springPageable = toPageable(pageRequest);

        Page<GoalSchema> goalSchemaPage = goalRepository.findAllByUserId(springPageable, userId);

        return goalMapper.toGoalPagedResult(goalSchemaPage, pageRequest);
    }



}
