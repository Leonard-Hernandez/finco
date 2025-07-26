package com.finco.finco.infrastructure.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.usecase.account.GetAccountUseCase;

@RestController
public class GetAccountController {

    private final GetAccountUseCase getAccountUseCase;

    public GetAccountController(GetAccountUseCase getAccountUseCase) {
        this.getAccountUseCase = getAccountUseCase;
    }

    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    public AccountPublicData getAccount(@PathVariable Long id) {
        return new AccountPublicData(getAccountUseCase.execute(id));
    }

}
