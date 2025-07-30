package com.finco.finco.infrastructure.goal.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.infrastructure.goal.dto.GoalTransactionData;
import com.finco.finco.usecase.goal.WithDrawGoalUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Goal", description = "Goal management endpoints")
public class WithDrawGoalController {

    private final WithDrawGoalUseCase withDrawGoalUseCase;

    public WithDrawGoalController(WithDrawGoalUseCase withDrawGoalUseCase) {
        this.withDrawGoalUseCase = withDrawGoalUseCase;
    }

    @PostMapping("/goals/{id}/withdraw")
    @LogExecution()
    @Operation(summary = "Withdraw from a goal", description = "Withdraw from a goal")
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
    public GoalPublicData withdraw(@PathVariable Long id, @Valid @RequestBody GoalTransactionData data) {
        return new GoalPublicData(withDrawGoalUseCase.execute(id, data));
    }

}
