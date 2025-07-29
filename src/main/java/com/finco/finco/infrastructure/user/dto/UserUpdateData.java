package com.finco.finco.infrastructure.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.finco.finco.usecase.user.dto.IUserUpdateData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User update data")
public record UserUpdateData(
        @NotBlank(message = "Name cannot be blank")
        @Schema(description = "User name", required = true)
        String name,
        @NotNull(message = "Enable cannot be null")
        @Schema(description = "User enable", required = true)
        Boolean enable) implements IUserUpdateData {

}
