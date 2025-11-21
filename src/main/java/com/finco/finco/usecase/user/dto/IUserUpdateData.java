package com.finco.finco.usecase.user.dto;

import com.finco.finco.entity.account.model.CurrencyEnum;

public interface IUserUpdateData {
    
    String name();
    Boolean enable();
    CurrencyEnum defaultCurrency();

}
