package com.finco.finco.infrastructure.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.infrastructure.user.dto.UserUpdateData;
import com.finco.finco.usecase.user.UpdateUserUseCase;

@RestController
public class UpdateUserController {

    private final UpdateUserUseCase updateUserUseCase;

    public UpdateUserController(UpdateUserUseCase updateUserUseCase) {
        this.updateUserUseCase = updateUserUseCase;
    }

    @PutMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserPublicData updateUser(@RequestBody UserUpdateData data, @PathVariable Long id) {

        return new UserPublicData(updateUserUseCase.execute(id, data));

    }

}
