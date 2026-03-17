package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.finco.finco.usecase.account.dto.IAccountTransactionData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account transaction data to deposit or withdraw")
public record AccountTransactionData(
    @Schema(description = "Amount of the transaction", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    BigDecimal amount,

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    @Schema(description = "Category of the transaction")
    String category,

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Description of the transaction")
    String description
    ) implements IAccountTransactionData {

}
