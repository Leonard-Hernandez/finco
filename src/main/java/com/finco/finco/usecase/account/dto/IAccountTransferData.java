package com.finco.finco.usecase.account.dto;

import java.math.BigDecimal;

public interface IAccountTransferData {

    BigDecimal amount();
    Long transferAccountId();
    String category();
    String description();

}
