package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.infrastructure.goal.dto.GoalUpdateData;
import com.finco.finco.usecase.goal.UpdateGoalUseCase;

@RestController
public class PatchGoalController {

    private final UpdateGoalUseCase updateGoalUseCase;

    public PatchGoalController(UpdateGoalUseCase updateGoalUseCase) {
        this.updateGoalUseCase = updateGoalUseCase;
    }

    @PatchMapping("/goals/{id}")
    @LogExecution()
    public GoalPublicData updateGoal(@PathVariable Long id, @RequestBody GoalUpdateData data) {
        return new GoalPublicData(updateGoalUseCase.execute(id, data));
    }

}
