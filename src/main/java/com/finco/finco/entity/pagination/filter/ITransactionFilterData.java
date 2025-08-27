package com.finco.finco.entity.pagination.filter;

import java.time.LocalDate;

import com.finco.finco.entity.transaction.model.TransactionType;

public interface ITransactionFilterData {

    Long userId();
    Long accountId();
    Long goalId();
    Long transferAccountId();
    String category();
    TransactionType type();
    Boolean onlyAccountTransactions();
    Boolean onlyGoalTransactions();
    LocalDate startDate();
    LocalDate endDate();

}
