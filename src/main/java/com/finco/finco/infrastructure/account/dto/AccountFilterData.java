package com.finco.finco.infrastructure.account.dto;

import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.entity.pagination.filter.IAccountFilterData;

public record AccountFilterData(Long userId, CurrencyEnum currency, AccountType type, Boolean enable) implements IAccountFilterData {

}
