package com.finco.finco.infrastructure.user.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.usecase.user.GetAllUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class GetAllUserController {

    private final GetAllUserUseCase getAllUserUseCase;

    public GetAllUserController(GetAllUserUseCase getAllUserUseCase) {
        this.getAllUserUseCase = getAllUserUseCase;
    }

    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Get all users", description = "Get all users you need to be admin to get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found successfully", 
                content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be admin to get all users", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public Page<UserPublicData> findAllUsers(@PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.ASC) Pageable pageable) {
        PageRequest domainPageRequest = toPageRequest(pageable);

        PagedResult<User> usersPagedResult = getAllUserUseCase.execute(domainPageRequest);

        return toPage(usersPagedResult, pageable).map(UserPublicData::new);
    }

}
