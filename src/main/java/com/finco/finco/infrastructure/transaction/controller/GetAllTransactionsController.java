package com.finco.finco.infrastructure.transaction.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPage;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageable;
import com.finco.finco.infrastructure.config.error.ErrorResponse;
import com.finco.finco.infrastructure.transaction.dto.TransactionFilterData;
import com.finco.finco.infrastructure.transaction.dto.TransactionPublicData;
import com.finco.finco.usecase.transaction.GetAllTransactionUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Transaction", description = "Transaction management endpoints")
public class GetAllTransactionsController {

    private final GetAllTransactionUseCase getAllTransactionsUseCase;

    public GetAllTransactionsController(GetAllTransactionUseCase getAllTransactionsUseCase) {
        this.getAllTransactionsUseCase = getAllTransactionsUseCase;
    }

    @GetMapping("/admin/transactions/")
    @LogExecution()
    @Operation(summary = "Get all transactions by goal", description = "Get all transactions by goal")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", description = "Transactions retrieved successfully", 
                content = @Content(schema = @Schema(implementation = TransactionPublicData.class))),
            @ApiResponse(
                responseCode = "400", description = "Invalid input", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                responseCode = "403", description = "Forbidden: You need to be admin to get all transactions", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                responseCode = "500", description = "Internal server error", 
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    @SecurityRequirement(name = "bearerAuth")
    public Page<TransactionPublicData> getAllTransactionsByGoal(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "accountId", required = false) Long accountId,
            @RequestParam(name = "goalId", required = false) Long goalId,
            @RequestParam(name = "transferAccountId", required = false) Long transferAccountId,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "type", required = false) TransactionType type) {

        PageRequest domainPageRequest = toPageRequest(page, size, sortBy, sortDirection);

        TransactionFilterData transactionFilterData = new TransactionFilterData(userId, accountId, goalId,
                transferAccountId, category, type);

        PagedResult<Transaction> transactionsPagedResult = getAllTransactionsUseCase.execute(domainPageRequest,
                transactionFilterData);

        return toPage(transactionsPagedResult, toPageable(domainPageRequest)).map(TransactionPublicData::new);
    }

}
