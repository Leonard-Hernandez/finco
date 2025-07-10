package com.finco.finco.usecase.goalaccountbalance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IGoalAccountBalanceData {

    Long id();
    Long goalId();
    Long accountId();
    BigDecimal balance();
    LocalDateTime lastUpdated();
    LocalDateTime createdAt();

}
