package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.finco.finco.usecase.account.dto.IAccountTransferData;

public record AccountTransferData(
    @NotNull(message = "Transfer account ID is required")
    Long transferAccountId, 
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    BigDecimal amount, 
    String category, 
    String description
) implements IAccountTransferData {

}
