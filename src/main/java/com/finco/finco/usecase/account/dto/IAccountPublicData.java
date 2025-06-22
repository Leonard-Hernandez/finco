package com.finco.finco.usecase.account.dto;

import java.time.LocalDateTime;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;

public interface IAccountPublicData {

    Long id();
    Long userId();
    String name();
    AccountType type();
    Long balance();
    CurrencyEnum currency();
    LocalDateTime creationDate();
    String description();
    boolean isDefault();
    boolean enable();

}
