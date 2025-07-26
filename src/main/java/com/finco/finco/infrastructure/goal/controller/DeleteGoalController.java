package com.finco.finco.infrastructure.goal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.DeleteGoalUseCase;

@RestController
public class DeleteGoalController {

    private final DeleteGoalUseCase deleteGoalUseCase;

    public DeleteGoalController(DeleteGoalUseCase deleteGoalUseCase) {
        this.deleteGoalUseCase = deleteGoalUseCase;
    }

    @DeleteMapping("/goals/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    public GoalPublicData deleteGoal(@PathVariable Long id) {
        return new GoalPublicData(deleteGoalUseCase.execute(id));  
    }

}
