package com.finco.finco.infrastructure.account.controller;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountTransferData;
import com.finco.finco.usecase.account.TransferAccountUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Account", description = "Account management endpoints")
public class TransferAccountController {

    private final TransferAccountUseCase transferAccountUseCase;

    public TransferAccountController(TransferAccountUseCase transferAccountUseCase) {
        this.transferAccountUseCase = transferAccountUseCase;
    }

    @PostMapping("/accounts/{id}/transfer")
    @LogExecution()
    @Operation(summary = "Transfer money from an account to another", description = "Transfer money from an account to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully", 
                content = @Content(schema = @Schema(implementation = AccountPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input, insufficient balance or invalid exchange rate", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to transfer money from an account to another", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    @Tool(description = "Transfer money from an account to another")
    public AccountPublicData transfer(@PathVariable Long id, @Valid @RequestBody AccountTransferData data) {
        return new AccountPublicData(transferAccountUseCase.execute(id, data));
    }

}
