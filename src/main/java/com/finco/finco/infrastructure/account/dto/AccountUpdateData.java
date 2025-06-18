package com.finco.finco.infrastructure.account.dto;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountUpdateData;

public record AccountUpdateData(
        String name,
        AccountType type,
        CurrencyEnum currency,
        String description,
        Boolean isDefault) implements IAccountUpdateData {

}
