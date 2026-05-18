package com.finco.finco.infrastructure.transaction.gateway;

import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.model.TransactionType;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;
import com.finco.finco.infrastructure.transaction.dto.TransactionFilterData;
import com.finco.finco.infrastructure.transaction.dto.TransactionPublicData;
import com.finco.finco.usecase.transaction.GetAllTransactionsByUserUseCase;

@Service
public class TransactionAiTools {

    private final GetAllTransactionsByUserUseCase tool;
    private final AuthGateway authGateway;

    public TransactionAiTools(GetAllTransactionsByUserUseCase tool, AuthGateway authGateway) {
        this.tool = tool;
        this.authGateway = authGateway;
    }

    @Tool(description = "List transactions for the authenticated user with optional filters (account, goal, category, type, date range). Use to answer questions about spending history, recent transactions, or category totals.")
    @LogExecution(logArguments = false, logReturnValue = false)
    public List<TransactionPublicData> getAllTransactionsByUser(
            @ToolParam(description = "Page number (0-based). Default 0", required = false) Integer page,
            @ToolParam(description = "Page size. Default 20", required = false) Integer size,
            @ToolParam(description = "Sort field. Default id", required = false) String sortBy,
            @ToolParam(description = "Sort direction asc|desc. Default desc", required = false) String sortDirection,
            @ToolParam(description = "Filter by account ID", required = false) Long accountId,
            @ToolParam(description = "Filter by goal ID", required = false) Long goalId,
            @ToolParam(description = "Filter by transfer target account ID", required = false) Long transferAccountId,
            @ToolParam(description = "Filter by category name", required = false) String category,
            @ToolParam(description = "Filter by transaction type (DEPOSIT, WITHDRAW, TRANSFER)", required = false) TransactionType type,
            @ToolParam(description = "Only account-linked transactions", required = false) Boolean onlyAccountTransactions,
            @ToolParam(description = "Only goal-linked transactions", required = false) Boolean onlyGoalTransactions,
            @ToolParam(description = "Start date inclusive (YYYY-MM-DD)", required = false) LocalDate startDate,
            @ToolParam(description = "End date inclusive (YYYY-MM-DD)", required = false) LocalDate endDate) {

        Long userId = authGateway.getAuthenticatedUserId();
        PageRequest domainPageRequest = toPageRequest(page == null ? 0 : page, size == null ? 20 : size, sortBy, sortDirection);
        TransactionFilterData transactionFilterData = new TransactionFilterData(userId, accountId, goalId,
                transferAccountId, category, type, onlyAccountTransactions, onlyGoalTransactions, startDate, endDate);
        return tool.execute(domainPageRequest, transactionFilterData).getContent().stream().map(TransactionPublicData::new).toList();
    }

}
