package com.finco.finco.infrastructure.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.usecase.user.DeleteUserUseCase;

@RestController
public class DeleteUserController {

    private final DeleteUserUseCase deleteUserUseCase;

    public DeleteUserController(DeleteUserUseCase deleteUserUseCase) {
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    public UserPublicData deleteUser(@PathVariable Long id) {
        return new UserPublicData(deleteUserUseCase.execute(id));
    }

}
