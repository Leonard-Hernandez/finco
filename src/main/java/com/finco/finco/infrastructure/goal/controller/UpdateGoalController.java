package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.infrastructure.goal.dto.GoalUpdateData;
import com.finco.finco.usecase.goal.UpdateGoalUseCase;

import jakarta.validation.Valid;

@RestController
public class UpdateGoalController {

    private final UpdateGoalUseCase updateGoalUseCase;

    public UpdateGoalController(UpdateGoalUseCase updateGoalUseCase) {
        this.updateGoalUseCase = updateGoalUseCase;
    }

    @PutMapping("/goals/{id}")
    @LogExecution()
    public GoalPublicData updateGoal(@PathVariable Long id, @Valid @RequestBody GoalUpdateData data) {
        return new GoalPublicData(updateGoalUseCase.execute(id, data));
    }

}
