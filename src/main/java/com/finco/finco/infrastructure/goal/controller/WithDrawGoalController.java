package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.goal.dto.GoalTransactionData;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.WithDrawGoalUseCase;

import jakarta.validation.Valid;

@RestController
public class WithDrawGoalController {

    private final WithDrawGoalUseCase withDrawGoalUseCase;

    public WithDrawGoalController(WithDrawGoalUseCase withDrawGoalUseCase) {
        this.withDrawGoalUseCase = withDrawGoalUseCase;
    }

    @PostMapping("/goals/{id}/withdraw")
    public GoalPublicData withdraw(@PathVariable Long id, @Valid @RequestBody GoalTransactionData data) {
        return new GoalPublicData(withDrawGoalUseCase.execute(id, data));
    }

}
