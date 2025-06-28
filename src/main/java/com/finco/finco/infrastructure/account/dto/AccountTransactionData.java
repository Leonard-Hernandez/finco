package com.finco.finco.infrastructure.account.dto;

import java.math.BigDecimal;

import com.finco.finco.usecase.account.dto.IAccountTransactionData;

public record AccountTransactionData(BigDecimal amount) implements IAccountTransactionData{

}
