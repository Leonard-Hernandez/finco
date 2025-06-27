package com.finco.finco.entity.exception;

public class AmountMustBeGreaterThanZeroException extends EbusinessException{

    public AmountMustBeGreaterThanZeroException() {
        super("Amount must be greater than zero");
    }

}
