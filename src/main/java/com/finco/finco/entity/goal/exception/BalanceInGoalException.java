package com.finco.finco.entity.goal.exception;

import com.finco.finco.entity.exception.EbusinessException;

public class BalanceInGoalException extends EbusinessException{

    public BalanceInGoalException() {
        super("This balance is already in a goal");
    }

}
