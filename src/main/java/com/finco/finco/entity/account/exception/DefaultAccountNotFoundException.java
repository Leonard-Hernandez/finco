package com.finco.finco.entity.account.exception;

import com.finco.finco.entity.EbusinessException;

public class DefaultAccountNotFoundException extends EbusinessException{

    public DefaultAccountNotFoundException() {
        super("Default Account Not Found");
    }

}
