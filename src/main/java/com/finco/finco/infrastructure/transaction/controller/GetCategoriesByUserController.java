package com.finco.finco.infrastructure.transaction.controller;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.usecase.transaction.GetCategoriesByUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Transaction", description = "Transaction management endpoints")
public class GetCategoriesByUserController {

    private final GetCategoriesByUserUseCase getCategoriesByUserUseCase;

    public GetCategoriesByUserController(GetCategoriesByUserUseCase getCategoriesByUserUseCase) {
        this.getCategoriesByUserUseCase = getCategoriesByUserUseCase;
    }

    @GetMapping("/transactions/categories/{userId}")
    @LogExecution()
    @Operation(summary = "Get categories by user", description = "Get categories by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories found successfully", 
                content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to get categories by user", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) 
        })
    @SecurityRequirement(name = "bearerAuth")
    @Tool(description = "Get categories by user")
    public List<String> getCategoriesByUser(@PathVariable Long userId) {
        return getCategoriesByUserUseCase.execute(userId);
    }

}
