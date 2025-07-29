package com.finco.finco.infrastructure.user.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.usecase.user.GetTotalBalanceByUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class GetTotalBalanceByUserController {

    private final GetTotalBalanceByUserUseCase getTotalBalanceByUser;

    public GetTotalBalanceByUserController(GetTotalBalanceByUserUseCase getTotalBalanceByUser) {
        this.getTotalBalanceByUser = getTotalBalanceByUser;
    }

    @GetMapping("/users/{id}/total-balance")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    @Operation(summary = "Get total balance by user", description = "Get total balance by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total balance found successfully", 
                content = @Content(schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to get total balance by user", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public BigDecimal getTotalBalanceByUser(@PathVariable Long id) {
        return getTotalBalanceByUser.execute(id);
    }

}
