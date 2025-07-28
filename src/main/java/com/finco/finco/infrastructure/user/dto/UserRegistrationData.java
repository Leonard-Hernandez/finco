package com.finco.finco.infrastructure.user.dto;

import com.finco.finco.infrastructure.user.validation.unique.UniqueEmail;
import com.finco.finco.usecase.user.dto.IUserRegistrationData;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationData(
    @NotBlank
    String name,
    @Email
    @UniqueEmail
    @NotBlank
    String email,
    @NotBlank
    String password
    ) implements IUserRegistrationData{}
