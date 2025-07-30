package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.finco.finco.usecase.account.dto.IAccountTransactionData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account transaction data to deposit or withdraw")
public record AccountTransactionData(
    @Schema(description = "Amount of the transaction", required = true)
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    BigDecimal amount, 

    @Schema(description = "Category of the transaction")
    String category, 
    
    @Schema(description = "Description of the transaction")
    String description
    ) implements IAccountTransactionData {

}
