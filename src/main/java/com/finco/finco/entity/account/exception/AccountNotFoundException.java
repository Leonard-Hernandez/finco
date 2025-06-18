package com.finco.finco.entity.account.exception;

import com.finco.finco.entity.EbusinessException;

public class AccountNotFoundException extends EbusinessException{

    public AccountNotFoundException() {
        super("Account Not Found");
    }

}
