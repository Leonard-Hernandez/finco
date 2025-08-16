package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;

import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountTotalData;

public record AccountTotalData(
    BigDecimal total,
    CurrencyEnum currency
) implements IAccountTotalData {

}
