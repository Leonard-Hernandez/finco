package com.finco.finco.infrastructure.account.dto;

import com.finco.finco.usecase.account.dto.IAccountTransactionData;

public record AccountTransactionData(Long amount) implements IAccountTransactionData{

}
