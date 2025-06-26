package com.finco.finco.infrastructure.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.finco.finco.usecase.user.dto.IUserUpdateData;

public record UserUpdateData(
        @NotNull(message = "ID cannot be null")
        Long id,
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotNull(message = "Enable cannot be null")
        Boolean enable) implements IUserUpdateData {

}
