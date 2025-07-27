package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountRegistrationData;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AccountRegistrationData(
        @NotBlank(message = "Account name is required")
        @Size(min = 2, max = 100, message = "Account name must be between 2 and 100 characters")
        String name,
        @NotNull(message = "Account type is required")
        AccountType type,
        @NotNull(message = "Initial balance is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be positive or zero")
        BigDecimal balance,
        @NotNull(message = "Currency is required")
        CurrencyEnum currency,
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        @PositiveOrZero(message = "Withdraw fee must be positive or zero")
        Double withdrawFee,
        @PositiveOrZero(message = "Deposit fee must be positive or zero")
        Double depositFee) implements IAccountRegistrationData {
}
