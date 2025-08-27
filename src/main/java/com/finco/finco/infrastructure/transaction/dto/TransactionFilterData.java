package com.finco.finco.infrastructure.transaction.dto;

import java.time.LocalDate;

import com.finco.finco.entity.pagination.filter.ITransactionFilterData;
import com.finco.finco.entity.transaction.model.TransactionType;

public record TransactionFilterData(Long userId, Long accountId, Long goalId, Long transferAccountId, String category,
        TransactionType type, Boolean onlyAccountTransactions, Boolean onlyGoalTransactions, LocalDate startDate, LocalDate endDate) implements ITransactionFilterData {

}
