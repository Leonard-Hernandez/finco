package com.finco.finco.infrastructure.goal.gateway;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.db.mapper.GoalMapper;
import com.finco.finco.infrastructure.config.db.repository.GoalRepository;
import com.finco.finco.infrastructure.config.db.schema.GoalSchema;

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
        Sort sort = pageRequest.getSortBy()
                .map(sortBy -> {
                    Sort.Direction direction = pageRequest.getSortDirection()
                            .filter(d -> d.equalsIgnoreCase("desc"))
                            .map(d -> Sort.Direction.DESC)
                            .orElse(Sort.Direction.ASC);
                    return Sort.by(direction, sortBy);
                })
                .orElse(Sort.unsorted());

        Pageable springPageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                sort);

        Page<GoalSchema> goalSchemaPage = goalRepository.findAllByUserId(springPageable, userId);

        return goalMapper.toGoalPagedResult(goalSchemaPage, pageRequest);
    }



}
