package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.infrastructure.goal.dto.GoalRegistrationData;
import com.finco.finco.usecase.goal.CreateGoalUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class CreateGoalController {

    private final CreateGoalUseCase createGoalUseCase;

    public CreateGoalController(CreateGoalUseCase createGoalUseCase) {
        this.createGoalUseCase = createGoalUseCase;
    }

    @PostMapping("/users/{userId}/goals")
    @ResponseStatus(HttpStatus.CREATED)
    @LogExecution()
    @Operation(summary = "Create a new goal", description = "Create a new goal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Goal created successfully", 
                content = @Content(schema = @Schema(implementation = GoalPublicData.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to create a goal", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) 
            })
    @SecurityRequirement(name = "bearerAuth")
    public GoalPublicData createGoal(@PathVariable Long userId, @Valid @RequestBody GoalRegistrationData goalRegistrationData) {
        return new GoalPublicData(createGoalUseCase.execute(userId, goalRegistrationData));
    }

}
