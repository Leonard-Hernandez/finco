package com.finco.finco.infrastructure.user.dto;

import com.finco.finco.usecase.user.dto.IUserUpdateData;

public record UserUpdateData(
        Long id,
        String name,
        Boolean enable) implements IUserUpdateData {

}
