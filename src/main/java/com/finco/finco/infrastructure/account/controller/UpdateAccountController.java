package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountUpdateData;
import com.finco.finco.usecase.account.UpdateAccountUseCase;

@RestController
public class UpdateAccountController {

    private UpdateAccountUseCase updateAccountUseCase;

    public UpdateAccountController(UpdateAccountUseCase updateAccountUseCase) {
        this.updateAccountUseCase = updateAccountUseCase;
    }

    @PutMapping("/account/{id}")
    public AccountPublicData updateAccount(@PathVariable Long id, @RequestBody AccountUpdateData data) {
        return new AccountPublicData(updateAccountUseCase.execute(id, data));
    }

}
