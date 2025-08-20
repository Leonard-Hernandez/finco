package com.finco.finco.usecase.user.dto;

import java.time.LocalDateTime;

import com.finco.finco.entity.account.model.CurrencyEnum;

public interface IUserPublicData {

    String id();
    String name();
    String email();
    CurrencyEnum defaultCurrency();
    LocalDateTime registrationDate();
    Boolean enable();

}
