package com.finco.finco.entity.security.exception;

import com.finco.finco.entity.exception.EbusinessException;

public class IdentityProviderUnavailableException extends EbusinessException {

    public IdentityProviderUnavailableException() {
        super("Identity provider is unavailable");
    }

}
