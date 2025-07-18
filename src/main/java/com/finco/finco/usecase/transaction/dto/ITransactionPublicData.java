package com.finco.finco.usecase.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.transaction.model.TransactionType;

public interface ITransactionPublicData {

    Long id();
    Long userId();
    Long accountId();
    TransactionType type();
    BigDecimal amount();
    BigDecimal fee();
    LocalDateTime date();
    String description();
    String category();
    Long goalId();
    Long transferAccountId();
    BigDecimal exchangeRate();

}
