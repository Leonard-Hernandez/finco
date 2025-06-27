package com.finco.finco.entity.exception;

public class InsufficientBalanceException extends EbusinessException{

    public InsufficientBalanceException() {
        super("Insufficient balance");
    }

}
