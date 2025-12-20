package com.finco.finco.infrastructure.transaction.gateway;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.infrastructure.transaction.dto.TransactionFilterData;
import com.finco.finco.infrastructure.transaction.dto.TransactionPublicData;
import com.finco.finco.usecase.transaction.GetAllTransactionsByUserUseCase;

@Service
public class TransactionAiTools {

    private final GetAllTransactionsByUserUseCase tool;

    public TransactionAiTools(GetAllTransactionsByUserUseCase tool) {
        this.tool = tool;
    }

    @Tool(description = "Get all transactions by user and filters")
    public List<TransactionPublicData> getAllTransactionsByUser(
            @ToolParam(description = "Page, default 0", required = true) Integer page,
            @ToolParam(description = "Size, default 20", required = true) Integer size,
            @ToolParam(description = "Sort by, default id", required = false) String sortBy,
            @ToolParam(description = "Sort direction, default desc", required = false) String sortDirection,
            @ToolParam(description = "Account id", required = false) Long accountId,
            @ToolParam(description = "Goal id", required = false) Long goalId,
            @ToolParam(description = "Transfer account id", required = false) Long transferAccountId,
            @ToolParam(description = "Category", required = false) String category,
            @ToolParam(description = "Type", required = false) TransactionType type,
            @ToolParam(description = "Only account transactions", required = false) Boolean onlyAccountTransactions,
            @ToolParam(description = "Only goal transactions", required = false) Boolean onlyGoalTransactions,
            @ToolParam(description = "Start date", required = false) LocalDate startDate,
            @ToolParam(description = "End date", required = false) LocalDate endDate,
            @ToolParam(description = "User id", required = true) Long userId) {

        PageRequest domainPageRequest = toPageRequest(page, size, sortBy, sortDirection);
        TransactionFilterData transactionFilterData = new TransactionFilterData(userId, accountId, goalId,
                transferAccountId, category, type, onlyAccountTransactions, onlyGoalTransactions, startDate, endDate);
        return tool.execute(domainPageRequest, transactionFilterData).getContent().stream().map(TransactionPublicData::new).toList();
    }

}
