package com.finco.finco.infrastructure.user.dto;

import com.finco.finco.usecase.user.dto.IUserRegistrationData;

public record UserRegistrationData(
    String name,
    String email,
    String password
    ) implements IUserRegistrationData{}
