package com.finco.finco.infrastructure.transaction.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.transaction.dto.TransactionPublicData;
import com.finco.finco.usecase.transaction.GetAllTransactionsByUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
@Tag(name = "User", description = "User management endpoints")
public class GetAllTransactionsByUserController {

    private final GetAllTransactionsByUserUseCase getAllTransactionsByUserUseCase;

    public GetAllTransactionsByUserController(GetAllTransactionsByUserUseCase getAllTransactionsByUserUseCase) {
        this.getAllTransactionsByUserUseCase = getAllTransactionsByUserUseCase;
    }

    @GetMapping("/users/{userId}/transactions")
    @LogExecution()
    @Operation(summary = "Get all transactions by user", description = "Get all transactions by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found successfully", 
                content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to get all transactions by user", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) 
        })
    @SecurityRequirement(name = "bearerAuth")
    public Page<TransactionPublicData> getAllTransactionsByUser(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.DESC) Pageable pageable, @PathVariable Long userId) {
        PageRequest domainPageRequest = toPageRequest(pageable);
        PagedResult<Transaction> transactionsPagedResult = getAllTransactionsByUserUseCase.execute(domainPageRequest,
                userId);

        Page<TransactionPublicData> responsePage = toPage(transactionsPagedResult, pageable)
                .map(TransactionPublicData::new);

        return responsePage;
    }

}
