package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountRegistrationData;

public record AccountRegistrationData(
        Long userId,
        String name,
        AccountType type,
        BigDecimal balance,
        CurrencyEnum currency,
        String description) implements IAccountRegistrationData {

}
