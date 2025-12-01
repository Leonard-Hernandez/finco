package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.usecase.account.DepositAccountUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Account", description = "Account management endpoints")
public class DepositAccountController {

    private final DepositAccountUseCase depositAccountUseCase;

    public DepositAccountController(DepositAccountUseCase depositAccountUseCase) {
        this.depositAccountUseCase = depositAccountUseCase;
    }

    @PostMapping("/accounts/{id}/deposit")
    @LogExecution()
    @Operation(summary = "Deposit money into an account", description = "Deposit money into an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully", 
                content = @Content(schema = @Schema(implementation = AccountPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to deposit money into an account", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public AccountPublicData deposit(@PathVariable Long id, @Valid @RequestBody AccountTransactionData data) {
        return new AccountPublicData(depositAccountUseCase.execute(id, data));
    }

}
