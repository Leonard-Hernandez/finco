package com.finco.finco.usecase.goal.dto;

import java.math.BigDecimal;

public interface IGoalTransactionData {

    Long accountId();

    BigDecimal amount();

    String description();

    String category();

}
