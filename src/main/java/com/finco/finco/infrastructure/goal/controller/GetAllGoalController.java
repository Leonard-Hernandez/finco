package com.finco.finco.infrastructure.goal.controller;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPage;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageable;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.goal.dto.GoalFilterData;
import com.finco.finco.infrastructure.goal.dto.GoalPublicData;
import com.finco.finco.usecase.goal.GetAllGoalsUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Goal", description = "Goal management endpoints")
public class GetAllGoalController {

    private final GetAllGoalsUseCase getAllGoalsUseCase;

    public GetAllGoalController(GetAllGoalsUseCase getAllGoalsUseCase) {
        this.getAllGoalsUseCase = getAllGoalsUseCase;
    }

    @GetMapping("/admin/goals")
    @LogExecution()
    @Operation(summary = "Get all goals", description = "Get all goals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Goals retrieved successfully", 
                content = @Content(schema = @Schema(implementation = GoalPublicData.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be admin to get all goals", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public Page<GoalPublicData> getAllGoals(@RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "accountId", required = false) Long accountId) {
                
        PageRequest domainPageRequest = toPageRequest(page, size, sortBy, sortDirection);

        GoalFilterData goalFilterData = new GoalFilterData(userId, accountId);

        PagedResult<Goal> goalsPagedResult = getAllGoalsUseCase.execute(domainPageRequest, goalFilterData);

        Page<GoalPublicData> responsePage = toPage(goalsPagedResult, toPageable(domainPageRequest)).map(GoalPublicData::new);

        return responsePage;
    }

}
