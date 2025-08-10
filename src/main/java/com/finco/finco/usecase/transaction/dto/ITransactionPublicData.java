package com.finco.finco.usecase.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.usecase.account.dto.IAccountLightPublicData;
import com.finco.finco.usecase.goal.dto.IGoalLightPublicData;

public interface ITransactionPublicData {

    Long id();
    Long userId();
    IAccountLightPublicData account();
    TransactionType type();
    BigDecimal amount();
    BigDecimal fee();
    LocalDateTime date();
    String description();
    String category();
    IGoalLightPublicData goal();
    IAccountLightPublicData transferAccount();
    BigDecimal exchangeRate();

}
