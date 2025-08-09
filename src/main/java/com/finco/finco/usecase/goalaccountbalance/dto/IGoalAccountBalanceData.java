package com.finco.finco.usecase.goalaccountbalance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.usecase.account.dto.IAccountLightPublicData;

public interface IGoalAccountBalanceData {

    Long id();
    Long goalId();
    IAccountLightPublicData account();
    BigDecimal balance();
    LocalDateTime lastUpdated();
    LocalDateTime createdAt();

}
