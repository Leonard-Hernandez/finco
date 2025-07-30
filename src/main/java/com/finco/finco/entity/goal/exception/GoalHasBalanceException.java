package com.finco.finco.entity.goal.exception;

import com.finco.finco.entity.exception.EbusinessException;

public class GoalHasBalanceException extends EbusinessException {

    public GoalHasBalanceException() {
        super("Goal has balance");
    }

}
