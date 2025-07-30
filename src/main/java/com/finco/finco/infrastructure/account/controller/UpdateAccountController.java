package com.finco.finco.infrastructure.account.controller;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountUpdateData;
import com.finco.finco.usecase.account.UpdateAccountUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.finco.finco.infrastructure.config.error.ErrorResponse;

@RestController
@Tag(name = "Account", description = "Account management endpoints")
public class UpdateAccountController {

    private UpdateAccountUseCase updateAccountUseCase;

    public UpdateAccountController(UpdateAccountUseCase updateAccountUseCase) {
        this.updateAccountUseCase = updateAccountUseCase;
    }

    @PutMapping("/accounts/{id}")
    @Validated
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Update an account", description = "Update an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully", 
                content = @Content(schema = @Schema(implementation = AccountPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to update an account", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public AccountPublicData updateAccount(@PathVariable Long id, @Valid @RequestBody AccountUpdateData data) {
        return new AccountPublicData(updateAccountUseCase.execute(id, data));
    }

}
