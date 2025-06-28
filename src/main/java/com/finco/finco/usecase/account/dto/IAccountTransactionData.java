package com.finco.finco.usecase.account.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;

public interface IAccountTransactionData {

    @Positive(message = "Amount must be greater than zero")
    BigDecimal amount();

}
