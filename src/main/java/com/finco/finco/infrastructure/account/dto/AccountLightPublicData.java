package com.finco.finco.infrastructure.account.dto;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountLightPublicData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account light public data")
public record AccountLightPublicData(
    @Schema(description = "Account id") 
    Long id, 
    @Schema(description = "Account name") 
    String name, 
    @Schema(description = "Account type")
    AccountType type, 
    @Schema(description = "Account currency") 
    CurrencyEnum currency)
        implements IAccountLightPublicData {

    public AccountLightPublicData(Account account) {
        this(account.getId(), account.getName(), account.getType(), account.getCurrency());
    }

}
