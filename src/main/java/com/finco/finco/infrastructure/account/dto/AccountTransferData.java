package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.finco.finco.usecase.account.dto.IAccountTransferData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account transfer data")
public record AccountTransferData(
    @NotNull(message = "Transfer account ID is required")
    @Schema(description = "Transfer account ID", requiredMode = Schema.RequiredMode.REQUIRED)
    Long transferAccountId, 
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    @Schema(description = "Amount", requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal amount, 
    @Schema(description = "Category")
    String category, 
    @Schema(description = "Description")
    String description,
    @Schema(description = "Exchange rate")
    BigDecimal exchangeRate
) implements IAccountTransferData {

}
