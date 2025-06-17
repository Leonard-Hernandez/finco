package com.finco.finco.entity.security.exception;

import com.finco.finco.entity.EbusinessException;

public class AccessDeniedBusinessException extends EbusinessException{

    public AccessDeniedBusinessException() {
        super("Access Denied for this resource");
    }

}
