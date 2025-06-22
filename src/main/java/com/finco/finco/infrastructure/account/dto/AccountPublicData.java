package com.finco.finco.infrastructure.account.dto;

import java.time.LocalDateTime;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountPublicData;

public record AccountPublicData(
    Long id,
    Long userId,
    String name,
    AccountType type,
    Long balance,
    CurrencyEnum currency,
    LocalDateTime creationDate,
    String description,
    boolean isDefault,
    boolean enable
) implements IAccountPublicData{

    public AccountPublicData(Account account){
        this(
            account.getId(),
            account.getUser().getId(),
            account.getName(),
            account.getType(),
            account.getBalance(),
            account.getCurrency(),
            account.getCreationDate(),
            account.getDescription(),
            account.isDefault(),
            account.isEnable()
        );
    }

}
