package com.finco.finco.infrastructure.user.dto;

import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.infrastructure.user.validation.unique.UniqueEmail;
import com.finco.finco.usecase.user.dto.IUserRegistrationData;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User registration data")
public record UserRegistrationData(

    @NotBlank
    @Schema(description = "User name", example = "John Doe")
    String name,
    @Email
    @UniqueEmail
    @NotBlank
    @Schema(description = "User email", example = "john.doe@example.com")
    String email,
    @NotBlank
    @Schema(description = "User password", example = "password")
    String password,
    @NotNull
    @Schema(description = "User default currency", example = "USD")
    CurrencyEnum defaultCurrency
    ) implements IUserRegistrationData{}
