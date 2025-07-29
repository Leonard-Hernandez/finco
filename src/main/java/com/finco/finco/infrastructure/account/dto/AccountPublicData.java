package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.account.model.AccountType;
import com.finco.finco.entity.account.model.CurrencyEnum;
import com.finco.finco.usecase.account.dto.IAccountPublicData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account public data")
public record AccountPublicData(
    @Schema(description = "Account id")
    Long id,
    @Schema(description = "User id")
    Long userId,
    @Schema(description = "Account name")
    String name,
    @Schema(description = "Account type")
    AccountType type,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "#.00")
    @Schema(description = "Account balance")
    BigDecimal balance,
    @Schema(description = "Account currency")
    CurrencyEnum currency,
    @Schema(description = "Account creation date")
    LocalDateTime creationDate,
    @Schema(description = "Account description")
    String description,
    @Schema(description = "Account is default")
    boolean isDefault,
    @Schema(description = "Account is enable")
    boolean isEnable,
    @Schema(description = "Account withdraw fee")
    Double withdrawFee,
    @Schema(description = "Account deposit fee")
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
