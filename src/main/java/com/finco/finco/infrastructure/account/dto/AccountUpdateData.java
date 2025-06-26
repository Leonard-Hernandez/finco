package com.finco.finco.infrastructure.account.dto;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountUpdateData;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountUpdateData(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotNull(message = "Type cannot be null")
        AccountType type,
        @NotNull(message = "Currency cannot be null")
        CurrencyEnum currency,
        String description,
        @NotNull(message = "IsDefault cannot be null")
        Boolean isDefault,
        @NotNull(message = "Enable cannot be null")
        Boolean enable) implements IAccountUpdateData {

}
