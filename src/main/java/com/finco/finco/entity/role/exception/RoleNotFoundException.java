package com.finco.finco.entity.role.exception;

import com.finco.finco.entity.exception.EbusinessException;

public class RoleNotFoundException extends EbusinessException{

    public RoleNotFoundException() {
        super("Role Not Found");
    }

}
