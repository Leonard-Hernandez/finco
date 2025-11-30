package com.finco.finco.infrastructure.transaction.controller;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.toPageRequest;

import java.time.LocalDate;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.infrastructure.transaction.dto.TransactionFilterData;
import com.finco.finco.usecase.transaction.GetAllTransactionsByUserUseCase;

@Component
public class TransactionTools {

    private final GetAllTransactionsByUserUseCase tool;

    public TransactionTools(GetAllTransactionsByUserUseCase tool) {
        this.tool = tool;
    }

    @Tool(description = "Get all transactions by user and filters")
    public PagedResult<Transaction> getAllTransactionsByUser(Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            Long accountId,
            Long goalId,
            Long transferAccountId,
            String category,
            TransactionType type,
            Boolean onlyAccountTransactions,
            Boolean onlyGoalTransactions,
            LocalDate startDate,
            LocalDate endDate,
            Long userId) {

        PageRequest domainPageRequest = toPageRequest(page, size, sortBy, sortDirection);
        TransactionFilterData transactionFilterData = new TransactionFilterData(userId, accountId, goalId,
                transferAccountId, category, type, onlyAccountTransactions, onlyGoalTransactions, startDate, endDate);
        return tool.execute(domainPageRequest, transactionFilterData);
    }

}
