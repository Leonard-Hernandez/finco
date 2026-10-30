package com.finco.finco.entity.security.exception;

import com.finco.finco.entity.exception.EbusinessException;

public class InvalidTokenException extends EbusinessException {

    public InvalidTokenException() {
        super("Access token was rejected by the identity provider");
    }

}
