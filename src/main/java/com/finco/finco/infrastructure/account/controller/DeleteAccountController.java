package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.usecase.account.DeleteAccountUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Account", description = "Account management endpoints")
public class DeleteAccountController {

    private final DeleteAccountUseCase deleteAccountUseCase;

    public DeleteAccountController(DeleteAccountUseCase deleteAccountUseCase) {
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    @DeleteMapping("/accounts/{id}")
    @LogExecution()
    @Operation(summary = "Delete an account", description = "Delete an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted successfully", 
                content = @Content(schema = @Schema(implementation = AccountPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or account is default", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to delete an account", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public AccountPublicData deleteAccount(@PathVariable Long id) {
        return new AccountPublicData(deleteAccountUseCase.execute(id));
    }

}
