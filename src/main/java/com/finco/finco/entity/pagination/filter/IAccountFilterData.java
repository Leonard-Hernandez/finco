package com.finco.finco.entity.pagination.filter;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;

public interface IAccountFilterData {

    public Long userId();
    public CurrencyEnum currency();
    public AccountType type();
    public Boolean enable();

}
