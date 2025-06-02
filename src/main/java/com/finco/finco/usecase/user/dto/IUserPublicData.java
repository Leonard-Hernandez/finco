package com.finco.finco.usecase.user.dto;

import java.time.LocalDateTime;

public interface IUserPublicData {

    String id();
    String name();
    String email();
    LocalDateTime registrationDate();
    Boolean enable();

}
