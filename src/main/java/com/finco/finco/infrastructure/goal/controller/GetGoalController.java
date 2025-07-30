package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.GetGoalUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Goal", description = "Goal management endpoints")
public class GetGoalController {

    private final GetGoalUseCase getGoalUseCase;

    public GetGoalController(GetGoalUseCase getGoalUseCase) {
        this.getGoalUseCase = getGoalUseCase;
    }

    @GetMapping("/goals/{id}")
    @LogExecution()
    @Operation(summary = "Get a goal", description = "Get a goal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Goal retrieved successfully", 
                content = @Content(schema = @Schema(implementation = GoalPublicData.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to get a goal", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public GoalPublicData getGoal(@PathVariable Long id) {
        return new GoalPublicData(getGoalUseCase.execute(id));
    }

}
