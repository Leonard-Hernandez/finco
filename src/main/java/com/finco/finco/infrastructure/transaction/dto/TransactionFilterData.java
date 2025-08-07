package com.finco.finco.infrastructure.transaction.dto;

import com.finco.finco.entity.pagination.filter.ITransactionFilterData;
import com.finco.finco.entity.transaction.model.TransactionType;

public record TransactionFilterData(Long userId, Long accountId, Long goalId, Long transferAccountId, String category,
        TransactionType type) implements ITransactionFilterData {

}
