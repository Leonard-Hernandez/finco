package com.finco.finco.usecase.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.finco.finco.usecase.goalaccountbalance.dto.IGoalAccountBalanceData;

public interface IGoalPublicData {

    Long id();
    String name();
    BigDecimal targetAmount();
    LocalDate deadLine();
    String description();
    LocalDateTime creationDate();
    boolean enable();
    List<IGoalAccountBalanceData> goalAccountBalances();

}
