package com.finco.finco.usecase.account.dto;

import java.math.BigDecimal;

import com.finco.finco.entity.account.model.CurrencyEnum;

public interface IAccountTotalData {

    BigDecimal total();
    CurrencyEnum currency();
}
