package com.finco.finco.infrastructure.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.infrastructure.user.dto.UserRegistrationData;
import com.finco.finco.usecase.user.CreateUserAdminUseCase;

import jakarta.validation.Valid;

@RestController
public class CreateUserAdminController {

    private final CreateUserAdminUseCase createUserAdminUseCase;

    public CreateUserAdminController(CreateUserAdminUseCase createUserAdminUseCase) {
        this.createUserAdminUseCase = createUserAdminUseCase;
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    @LogExecution(logArguments = false)
    public UserPublicData createUserAdmin(@RequestBody @Valid UserRegistrationData data) {

        return new UserPublicData(createUserAdminUseCase.execute(data));

    }

}
