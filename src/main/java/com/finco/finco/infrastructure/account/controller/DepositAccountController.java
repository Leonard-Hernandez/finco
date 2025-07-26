package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;
import com.finco.finco.usecase.account.DepositAccountUseCase;

import jakarta.validation.Valid;

@RestController
public class DepositAccountController {

    private final DepositAccountUseCase depositAccountUseCase;

    public DepositAccountController(DepositAccountUseCase depositAccountUseCase) {
        this.depositAccountUseCase = depositAccountUseCase;
    }

    @PostMapping("/accounts/{id}/deposit")
    @LogExecution()
    public AccountPublicData deposit(@PathVariable Long id, @Valid @RequestBody AccountTransactionData data) {
        return new AccountPublicData(depositAccountUseCase.execute(id, data));
    }

}
