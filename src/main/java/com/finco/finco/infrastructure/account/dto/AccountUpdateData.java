package com.finco.finco.infrastructure.account.dto;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountUpdateData;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account update data")
public record AccountUpdateData(
        @Schema(description = "Account name")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Schema(description = "Account type")
        @NotNull(message = "Type cannot be null")
        AccountType type,
        @Schema(description = "Account currency")
        @NotNull(message = "Currency cannot be null")
        CurrencyEnum currency,
        @Schema(description = "Account description")
        String description,
        @NotNull(message = "IsDefault cannot be null")
        @Schema(description = "Account is default")
        Boolean isDefault,
        @NotNull(message = "Enable cannot be null")
        @Schema(description = "Account enable")
        Boolean enable,
        @Schema(description = "Account withdraw fee")
        Double withdrawFee,
        @Schema(description = "Account deposit fee")
        Double depositFee) implements IAccountUpdateData {

}
