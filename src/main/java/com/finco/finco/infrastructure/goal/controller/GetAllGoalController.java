package com.finco.finco.infrastructure.goal.controller;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.GetAllGoalsUseCase;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
public class GetAllGoalController {

    private final GetAllGoalsUseCase getAllGoalsUseCase;

    public GetAllGoalController(GetAllGoalsUseCase getAllGoalsUseCase) {
        this.getAllGoalsUseCase = getAllGoalsUseCase;
    }

    @GetMapping("/admin/goals")
    @LogExecution()
    public Page<GoalPublicData> getAllGoals(@PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.DESC) Pageable pageable) {
        PageRequest domainPageRequest = toPageRequest(pageable);

        PagedResult<Goal> goalsPagedResult = getAllGoalsUseCase.execute(domainPageRequest);

        Page<GoalPublicData> responsePage = toPage(goalsPagedResult, pageable).map(GoalPublicData::new);

        return responsePage;
    }

}
