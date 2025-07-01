package com.finco.finco.infrastructure.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.infrastructure.user.dto.UserRegistrationData;
import com.finco.finco.usecase.user.CreateUserUseCase;

import jakarta.validation.Valid;

@RestController
public class CreateUserController {

    private final CreateUserUseCase createUserUseCase;

    public CreateUserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserPublicData createUser(@RequestBody @Valid UserRegistrationData data) {

        return new UserPublicData(createUserUseCase.execute(data));

    }

}
