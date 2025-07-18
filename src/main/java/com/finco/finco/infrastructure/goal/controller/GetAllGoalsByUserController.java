package com.finco.finco.infrastructure.goal.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.GetAllGoalsByUserUseCase;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
public class GetAllGoalsByUserController {

    private final GetAllGoalsByUserUseCase getAllGoalsByUserUseCase;

    public GetAllGoalsByUserController(GetAllGoalsByUserUseCase getAllGoalsByUserUseCase) {
        this.getAllGoalsByUserUseCase = getAllGoalsByUserUseCase;
    }

    @GetMapping("/users/{userId}/goals")
    public Page<GoalPublicData> getAllGoalsByUser(@PageableDefault(page = 0, size = 20, sort = "name") Pageable pageable, @PathVariable Long userId) {

        PageRequest domainPageRequest = toPageRequest(pageable);
        
        PagedResult<Goal> goalsPagedResult = getAllGoalsByUserUseCase.execute(userId, domainPageRequest);

        Page<GoalPublicData> responsePage = toPage(goalsPagedResult, pageable).map(GoalPublicData::new);

        return responsePage;
    }

}
