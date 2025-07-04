package com.finco.finco.infrastructure.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountRegistrationData;
import com.finco.finco.usecase.account.CreateAccountUseCase;

@RestController
public class CreateAccountController {

    private CreateAccountUseCase createAccountUseCase;

    public CreateAccountController(CreateAccountUseCase createAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
    }

    @PostMapping("/users/{userId}/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountPublicData createAccountForUser(
            @PathVariable Long userId,
            @RequestBody AccountRegistrationData data) {
        return new AccountPublicData(createAccountUseCase.execute(userId, data));
    }

}
