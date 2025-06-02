package com.finco.finco.infrastructure.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.infrastructure.user.dto.UserRegistrationData;
import com.finco.finco.usecase.user.CreateUserUseCase;

@RestController
public class CreateUserController {

    private final CreateUserUseCase createUserUseCase;

    public CreateUserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/user")
    public UserPublicData createUser(@RequestBody UserRegistrationData data) {

        return new UserPublicData(createUserUseCase.execute(data));

    }

}
