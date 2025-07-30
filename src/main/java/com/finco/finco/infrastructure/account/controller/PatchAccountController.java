package com.finco.finco.infrastructure.account.controller;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountUpdateData;
import com.finco.finco.usecase.account.UpdateAccountUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

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
public class PatchAccountController {

    private final UpdateAccountUseCase updateAccountUseCase;

    public PatchAccountController(UpdateAccountUseCase updateAccountUseCase) {
        this.updateAccountUseCase = updateAccountUseCase;
    }

    @PatchMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Update an account", description = "Patch an account")
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
    public AccountPublicData patchAccount(@RequestBody AccountUpdateData data, @PathVariable Long id) {
        return new AccountPublicData(updateAccountUseCase.execute(id, data));
    }

}
