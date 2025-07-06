package com.finco.finco.entity.account.exception;

import com.finco.finco.entity.exception.EbusinessException;

public class ExchangeRateNotFound extends EbusinessException{

    public ExchangeRateNotFound() {
        super("Exchange rate not found");
    }

}
