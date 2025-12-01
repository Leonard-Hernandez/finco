package com.finco.finco.infrastructure.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.usecase.account.GetAccountUseCase;

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
public class GetAccountController {

    private final GetAccountUseCase getAccountUseCase;

    public GetAccountController(GetAccountUseCase getAccountUseCase) {
        this.getAccountUseCase = getAccountUseCase;
    }

    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Get an account by id", description = "Get an account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found successfully", 
                content = @Content(schema = @Schema(implementation = AccountPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to get an account", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public AccountPublicData getAccount(@PathVariable Long id) {
        return new AccountPublicData(getAccountUseCase.execute(id));
    }

}
