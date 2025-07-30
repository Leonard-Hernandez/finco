package com.finco.finco.infrastructure.transaction.controller;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPage;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;

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
import com.finco.finco.usecase.transaction.GetAllTransactionsByAccountUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Account", description = "Account management endpoints")
public class GetAllTransactionsByAccountController {

    private final GetAllTransactionsByAccountUseCase getAllTransactionsByAccountUseCase;

    public GetAllTransactionsByAccountController(
            GetAllTransactionsByAccountUseCase getAllTransactionsByAccountUseCase) {
        this.getAllTransactionsByAccountUseCase = getAllTransactionsByAccountUseCase;
    }

    @GetMapping("/accounts/{accountId}/transactions")
    @LogExecution()
    @Operation(summary = "Get all transactions by account", description = "Get all transactions by account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully", 
                content = @Content(schema = @Schema(implementation = TransactionPublicData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: You need to be owner to update a goal", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public Page<TransactionPublicData> getAllTransactionsByAccount(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.DESC) Pageable pageable, @PathVariable Long accountId) {
        PageRequest domainPageRequest = toPageRequest(pageable);
        PagedResult<Transaction> transactionsPagedResult = getAllTransactionsByAccountUseCase.execute(domainPageRequest,
                accountId);

        Page<TransactionPublicData> responsePage = toPage(transactionsPagedResult, pageable)
                .map(TransactionPublicData::new);

        return responsePage;
    }
}
