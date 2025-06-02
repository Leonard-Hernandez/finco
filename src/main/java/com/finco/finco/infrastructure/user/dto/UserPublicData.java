package com.finco.finco.infrastructure.user.dto;

import java.time.LocalDateTime;

import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.user.dto.IUserPublicData;

public record UserPublicData(
        String id,
        String name,
        String email,
        LocalDateTime registrationDate,
        Boolean enable) implements IUserPublicData {

    public UserPublicData(User user){
        this(
            user.getId().toString(), 
            user.getName(), 
            user.getEmail(), 
            user.getRegistrationDate(), 
            user.getEnable()
        );
    }

}
