package com.finco.finco.infrastructure.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.usecase.user.GetUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class GetUserController {

    private final GetUserUseCase getUserUserCase;

    public GetUserController(GetUserUseCase getUserController) {
        this.getUserUserCase = getUserController;
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Get user by id", description = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully", 
                content = @Content(schema = @Schema(implementation = UserPublicData.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to get user by id", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public UserPublicData getUser(@PathVariable long id) {

        return new UserPublicData(getUserUserCase.execute(id));

    }

}
