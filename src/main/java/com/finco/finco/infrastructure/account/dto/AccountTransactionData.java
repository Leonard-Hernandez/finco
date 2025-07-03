package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.finco.finco.usecase.account.dto.IAccountTransactionData;

public record AccountTransactionData(
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    BigDecimal amount, 
    String category, 
    String description,
    BigDecimal fee
) implements IAccountTransactionData {

}
