package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.GetGoalUseCase;

@RestController
public class GetGoalController {

    private final GetGoalUseCase getGoalUseCase;

    public GetGoalController(GetGoalUseCase getGoalUseCase) {
        this.getGoalUseCase = getGoalUseCase;
    }

    @GetMapping("/goals/{id}")
    @LogExecution()
    public GoalPublicData getGoal(@PathVariable Long id) {
        return new GoalPublicData(getGoalUseCase.execute(id));
    }

}
