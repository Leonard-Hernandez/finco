package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.annotation.LogExecution;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Account", description = "Account management endpoints")
public class GetAccountTypesController {

    @GetMapping("/accounts/types")
    @LogExecution()
    @Operation(summary = "Get Account types", description = "Get Account types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account types found successfully", 
                content = @Content(schema = @Schema(implementation = AccountType[].class))) })
    @SecurityRequirement(name = "bearerAuth")
    public AccountType[] getAccountTypes() {
        return AccountType.values();
    }

}
