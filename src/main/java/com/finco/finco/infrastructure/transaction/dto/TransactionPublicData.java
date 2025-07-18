package com.finco.finco.infrastructure.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.usecase.transaction.dto.ITransactionPublicData;

public record TransactionPublicData(
        Long id,
        Long userId,
        Long accountId,
        TransactionType type,
        BigDecimal amount,
        BigDecimal fee,
        LocalDateTime date,
        String description,
        String category,
        Long goalId,
        Long transferAccountId,
        BigDecimal exchangeRate) implements ITransactionPublicData {

    public TransactionPublicData(Transaction transaction) {
        this(transaction.getId(), 
            transaction.getUser().getId(), 
            transaction.getAccount().getId(),
            transaction.getType(), 
            transaction.getAmount(), 
            transaction.getFee(), 
            transaction.getDate(),
            transaction.getDescription(), 
            transaction.getCategory(), 
            transaction.getGoal() != null ? transaction.getGoal().getId() : null,
            transaction.getTransferAccount() != null ? transaction.getTransferAccount().getId() : null,
            transaction.getExchangeRate());
    }

}
