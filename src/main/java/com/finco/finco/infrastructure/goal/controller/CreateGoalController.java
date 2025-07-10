package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.infrastructure.goal.dto.GoalRegistrationData;
import com.finco.finco.usecase.goal.CreateGoalUseCase;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
public class CreateGoalController {

    private final CreateGoalUseCase createGoalUseCase;

    public CreateGoalController(CreateGoalUseCase createGoalUseCase) {
        this.createGoalUseCase = createGoalUseCase;
    }

    @PostMapping("/users/{userId}/goals")
    @ResponseStatus(HttpStatus.CREATED)
    public GoalPublicData deposit(@PathVariable Long userId, @Valid @RequestBody GoalRegistrationData goalRegistrationData) {
        return new GoalPublicData(createGoalUseCase.execute(userId, goalRegistrationData));
    }

}
