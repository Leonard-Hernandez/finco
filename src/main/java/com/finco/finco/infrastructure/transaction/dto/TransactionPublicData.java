package com.finco.finco.infrastructure.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.usecase.transaction.dto.ITransactionPublicData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Transaction public data")
public record TransactionPublicData(@Schema(description = "Transaction ID", example = "1") Long id,
        @Schema(description = "User ID", example = "1") 
        Long userId,
        @Schema(description = "Account ID", example = "1") 
        Long accountId,
        @Schema(description = "Transaction type", example = "DEPOSIT") 
        TransactionType type,
        @Schema(description = "Transaction amount", example = "100") 
        BigDecimal amount,
        @Schema(description = "Transaction fee", example = "10") 
        BigDecimal fee,
        @Schema(description = "Transaction date", example = "2022-01-01T00:00:00") 
        LocalDateTime date,
        @Schema(description = "Transaction description", example = "Description") 
        String description,
        @Schema(description = "Transaction category", example = "Category") 
        String category,
        @Schema(description = "Goal ID", example = "1") 
        Long goalId,
        @Schema(description = "Transfer account ID", example = "1") 
        Long transferAccountId,
        @Schema(description = "Exchange rate", example = "1.0") 
        BigDecimal exchangeRate) implements ITransactionPublicData {

    public TransactionPublicData(Transaction transaction) {
        this(transaction.getId(), transaction.getUser().getId(), transaction.getAccount().getId(),
                transaction.getType(), transaction.getAmount(), transaction.getFee(), transaction.getDate(),
                transaction.getDescription(), transaction.getCategory(),
                transaction.getGoal() != null ? transaction.getGoal().getId() : null,
                transaction.getTransferAccount() != null ? transaction.getTransferAccount().getId() : null,
                transaction.getExchangeRate());
    }

}
