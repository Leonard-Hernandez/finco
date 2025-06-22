package com.finco.finco.entity.account.exception;

import com.finco.finco.entity.EbusinessException;

public class CannotDeactivateDefaultAccountException extends EbusinessException{

    public CannotDeactivateDefaultAccountException() {
        super("Cannot deactivate default account. A user must always have at least one default account.");
    }

}
