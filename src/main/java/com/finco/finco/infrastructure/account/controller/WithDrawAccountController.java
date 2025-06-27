package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;
import com.finco.finco.usecase.account.WithDrawAccountUseCase;

import jakarta.validation.Valid;

@RestController
public class WithDrawAccountController {

    private final WithDrawAccountUseCase withDrawAccountUseCase;

    public WithDrawAccountController(WithDrawAccountUseCase withDrawAccountUseCase) {
        this.withDrawAccountUseCase = withDrawAccountUseCase;
    }

    @PutMapping("/account/{id}/withdraw")
    public AccountPublicData withdraw(@PathVariable Long id, @Valid @RequestBody AccountTransactionData data) {
        return new AccountPublicData(withDrawAccountUseCase.execute(id, data));
    }

}
