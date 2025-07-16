package com.finco.finco.entity.goal.exception;

import com.finco.finco.entity.exception.EbusinessException;

public class AccountNotAssociatedWithGoalException extends EbusinessException {

    public AccountNotAssociatedWithGoalException() {
        super("Account not associated with goal");
    }

}
