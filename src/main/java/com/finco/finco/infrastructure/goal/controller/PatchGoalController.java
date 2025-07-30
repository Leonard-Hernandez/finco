package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.infrastructure.goal.dto.GoalUpdateData;
import com.finco.finco.usecase.goal.UpdateGoalUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Goal", description = "Goal management endpoints")
public class PatchGoalController {

    private final UpdateGoalUseCase updateGoalUseCase;

    public PatchGoalController(UpdateGoalUseCase updateGoalUseCase) {
        this.updateGoalUseCase = updateGoalUseCase;
    }

    @PatchMapping("/goals/{id}")
    @LogExecution()
    @Operation(summary = "Update a goal", description = "Patch a goal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Goal updated successfully", 
                content = @Content(schema = @Schema(implementation = GoalPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to update a goal", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public GoalPublicData updateGoal(@PathVariable Long id, @RequestBody GoalUpdateData data) {
        return new GoalPublicData(updateGoalUseCase.execute(id, data));
    }

}
