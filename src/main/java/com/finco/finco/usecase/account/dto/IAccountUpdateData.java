package com.finco.finco.usecase.account.dto;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;

public interface IAccountUpdateData {

    String name();
    AccountType type();
    Long balance();
    CurrencyEnum currency();
    String description();
    boolean isDefault();

}
