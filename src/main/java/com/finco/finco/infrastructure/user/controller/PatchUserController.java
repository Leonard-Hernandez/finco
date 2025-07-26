package com.finco.finco.infrastructure.user.controller;

import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.infrastructure.user.dto.UserUpdateData;
import com.finco.finco.usecase.user.UpdateUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class PatchUserController {

    private final UpdateUserUseCase updateUserUseCase;

    public PatchUserController(UpdateUserUseCase updateUserUseCase) {
        this.updateUserUseCase = updateUserUseCase;
    }

    @PatchMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    public UserPublicData patchUser(@RequestBody UserUpdateData data, @PathVariable Long id) {
        return new UserPublicData(updateUserUseCase.execute(id, data));
    }

}
