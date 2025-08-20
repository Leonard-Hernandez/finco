package com.finco.finco.infrastructure.user.dto;

import java.time.LocalDateTime;

import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserPublicData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User public data")
public record UserPublicData(
        @Schema(description = "User id", example = "1")
        String id,
        @Schema(description = "User name", example = "John Doe")
        String name,
        @Schema(description = "User email", example = "john.doe@example.com")
        String email,
        @Schema(description = "User default currency", example = "USD")
        CurrencyEnum defaultCurrency,
        @Schema(description = "User registration date", example = "2022-01-01T00:00:00")
        LocalDateTime registrationDate,
        @Schema(description = "User enable", example = "true")
        Boolean enable) implements IUserPublicData {

    public UserPublicData(User user){
        this(
            user.getId().toString(), 
            user.getName(), 
            user.getEmail(), 
            user.getDefaultCurrency(), 
            user.getRegistrationDate(), 
            user.isEnable()
        );
    }

}
