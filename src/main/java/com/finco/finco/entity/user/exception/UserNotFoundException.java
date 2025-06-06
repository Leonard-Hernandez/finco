package com.finco.finco.entity.user.exception;

import com.finco.finco.entity.EbusinessException;

public class UserNotFoundException extends EbusinessException{

    public UserNotFoundException() {
        super("User Not Found");
    }

}
