package com.finco.finco.usecase.account.dto;

import java.math.BigDecimal;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;

public interface IAccountRegistrationData {

    String name();
    AccountType type();
    BigDecimal balance();
    CurrencyEnum currency();
    String description();
    Double withdrawFee();
    Double depositFee();

}
