package com.finco.finco.infrastructure.goal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.DeleteGoalUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.finco.finco.infrastructure.config.error.ErrorResponse;

@RestController
@Tag(name = "Goal", description = "Goal management endpoints")
public class DeleteGoalController {

    private final DeleteGoalUseCase deleteGoalUseCase;

    public DeleteGoalController(DeleteGoalUseCase deleteGoalUseCase) {
        this.deleteGoalUseCase = deleteGoalUseCase;
    }

    @DeleteMapping("/goals/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Delete a goal", description = "Delete a goal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Goal deleted successfully", 
                content = @Content(schema = @Schema(implementation = GoalPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input, goal has balance", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to delete a goal", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public GoalPublicData deleteGoal(@PathVariable Long id) {
        return new GoalPublicData(deleteGoalUseCase.execute(id));  
    }

}
