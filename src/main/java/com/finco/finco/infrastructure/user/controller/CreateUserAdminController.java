package com.finco.finco.infrastructure.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.infrastructure.user.dto.UserRegistrationData;
import com.finco.finco.usecase.user.CreateUserAdminUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class CreateUserAdminController {

    private final CreateUserAdminUseCase createUserAdminUseCase;

    public CreateUserAdminController(CreateUserAdminUseCase createUserAdminUseCase) {
        this.createUserAdminUseCase = createUserAdminUseCase;
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    @LogExecution(logArguments = false)
    @Operation(summary = "Create a new user admin", description = "Create a new user admin you need to be admin to create a new user admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User admin created successfully", 
                content = @Content(schema = @Schema(implementation = UserPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be admin to create a new user admin", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found: is role not found", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public UserPublicData createUserAdmin(@RequestBody @Valid UserRegistrationData data) {

        return new UserPublicData(createUserAdminUseCase.execute(data));

    }

}
