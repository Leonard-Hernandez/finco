package com.finco.finco.infrastructure.user.controller;

import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.infrastructure.user.dto.UserUpdateData;
import com.finco.finco.usecase.user.UpdateUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.Valid;

@RestController
public class UpdateUserController {

    private final UpdateUserUseCase updateUserUseCase;

    public UpdateUserController(UpdateUserUseCase updateUserUseCase) {
        this.updateUserUseCase = updateUserUseCase;
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Validated
    public UserPublicData updateUser(@Valid @RequestBody UserUpdateData data, @PathVariable Long id) {
        return new UserPublicData(updateUserUseCase.execute(id, data));
    }

}
