package com.finco.finco.infrastructure.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.user.dto.IUserUpdateData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User update data")
public record UserUpdateData(
        @NotBlank(message = "Name cannot be blank")
        @Schema(description = "User name")
        String name,
        @NotNull(message = "Enable cannot be null")
        @Schema(description = "User enable")
        Boolean enable,
        @Schema(description = "User default currency")
        CurrencyEnum defaultCurrency
) implements IUserUpdateData {

}
