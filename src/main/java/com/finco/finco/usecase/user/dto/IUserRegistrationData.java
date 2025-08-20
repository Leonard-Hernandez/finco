package com.finco.finco.usecase.user.dto;

import com.finco.finco.entity.account.model.CurrencyEnum;

public interface IUserRegistrationData {

    String name();
    String email();
    String password();
    CurrencyEnum defaultCurrency();

}
