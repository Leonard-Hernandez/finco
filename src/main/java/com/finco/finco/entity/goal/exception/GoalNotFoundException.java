package com.finco.finco.entity.goal.exception;

import com.finco.finco.entity.exception.EbusinessException;

public class GoalNotFoundException extends EbusinessException {

    public GoalNotFoundException() {
        super("Goal not found");
    }

}
