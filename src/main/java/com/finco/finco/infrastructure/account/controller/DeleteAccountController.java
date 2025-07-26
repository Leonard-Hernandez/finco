package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.usecase.account.DeleteAccountUseCase;

@RestController
public class DeleteAccountController {

    private final DeleteAccountUseCase deleteAccountUseCase;

    public DeleteAccountController(DeleteAccountUseCase deleteAccountUseCase) {
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    @DeleteMapping("/accounts/{id}")
    @LogExecution()
    public AccountPublicData deleteAccount(@PathVariable Long id) {
        return new AccountPublicData(deleteAccountUseCase.execute(id));
    }

}
