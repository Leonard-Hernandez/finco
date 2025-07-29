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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class PatchUserController {

    private final UpdateUserUseCase updateUserUseCase;

    public PatchUserController(UpdateUserUseCase updateUserUseCase) {
        this.updateUserUseCase = updateUserUseCase;
    }

    @PatchMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Patch user by id", description = "Patch user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User patched successfully", 
                content = @Content(schema = @Schema(implementation = UserPublicData.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to patch user by id", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public UserPublicData patchUser(@RequestBody UserUpdateData data, @PathVariable Long id) {
        return new UserPublicData(updateUserUseCase.execute(id, data));
    }

}
