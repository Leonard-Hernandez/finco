package com.finco.finco.infrastructure.user.controller;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.infrastructure.user.dto.UserUpdateData;
import com.finco.finco.usecase.user.UpdateUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class UpdateUserController {

    private final UpdateUserUseCase updateUserUseCase;

    public UpdateUserController(UpdateUserUseCase updateUserUseCase) {
        this.updateUserUseCase = updateUserUseCase;
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Update user by id", description = "Update user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", 
                content = @Content(schema = @Schema(implementation = UserPublicData.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to update user by id", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public UserPublicData updateUser(@Valid @RequestBody UserUpdateData data, @PathVariable Long id) {
        return new UserPublicData(updateUserUseCase.execute(id, data));
    }

}
