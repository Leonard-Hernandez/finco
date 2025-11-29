package com.finco.finco.infrastructure.transaction.controller;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

import java.time.LocalDate;

import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.transaction.dto.TransactionFilterData;
import com.finco.finco.infrastructure.transaction.dto.TransactionPublicData;
import com.finco.finco.usecase.transaction.GetAllTransactionsByUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

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
            @ApiResponse(
                responseCode = "200", description = "Transactions found successfully", 
                content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(
                responseCode = "403", description = "Forbidden: You need to be owner to get all transactions by user", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @Tool(description = "Get all transactions by user")
    public Page<TransactionPublicData> getAllTransactionsByUser(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long goalId,
            @RequestParam(required = false) Long transferAccountId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Boolean onlyAccountTransactions,
            @RequestParam(required = false) Boolean onlyGoalTransactions,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @PathVariable Long userId) {
        PageRequest domainPageRequest = toPageRequest(page, size, sortBy, sortDirection);
        TransactionFilterData transactionFilterData = new TransactionFilterData(userId, accountId, goalId,
                transferAccountId, category, type, onlyAccountTransactions, onlyGoalTransactions, startDate, endDate);
        PagedResult<Transaction> transactionsPagedResult = getAllTransactionsByUserUseCase.execute(domainPageRequest,
                transactionFilterData);

        Page<TransactionPublicData> responsePage = toPage(transactionsPagedResult, toPageable(domainPageRequest))
                .map(TransactionPublicData::new);

        return responsePage;
    }

}
