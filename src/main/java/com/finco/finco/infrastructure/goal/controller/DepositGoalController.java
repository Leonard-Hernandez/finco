package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.goal.dto.GoalTransactionData;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.DepositGoalUseCase;

import jakarta.validation.Valid;

@RestController
public class DepositGoalController {

    private final DepositGoalUseCase depositGoalUseCase;

    public DepositGoalController(DepositGoalUseCase depositGoalUseCase) {
        this.depositGoalUseCase = depositGoalUseCase;
    }

    @PostMapping("/goals/{id}/deposit")
    @LogExecution()
    public GoalPublicData deposit(@PathVariable Long id, @Valid @RequestBody GoalTransactionData data) {
        return new GoalPublicData(depositGoalUseCase.execute(id, data));
    }

}
