package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountPublicData;

public record AccountPublicData(
    Long id,
    Long userId,
    String name,
    AccountType type,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "#.00")
    BigDecimal balance,
    CurrencyEnum currency,
    LocalDateTime creationDate,
    String description,
    boolean isDefault,
    boolean isEnable,
    Double withdrawFee,
    Double depositFee
) implements IAccountPublicData{

    public AccountPublicData(Account account){
        this(
            account.getId(),
            account.getUser().getId(),
            account.getName(),
            account.getType(),
            account.getBalance().setScale(2, RoundingMode.HALF_UP),
            account.getCurrency(),
            account.getCreationDate(),
            account.getDescription(),
            account.isDefault(),
            account.isEnable(),
            account.getWithdrawFee(),
            account.getDepositFee()
        );
    }

}
