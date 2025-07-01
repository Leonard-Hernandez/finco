package com.finco.finco.infrastructure.account.controller;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountUpdateData;
import com.finco.finco.usecase.account.UpdateAccountUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class PatchAccountController {

    private final UpdateAccountUseCase updateAccountUseCase;

    public PatchAccountController(UpdateAccountUseCase updateAccountUseCase) {
        this.updateAccountUseCase = updateAccountUseCase;
    }

    @PatchMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountPublicData patchAccount(@RequestBody AccountUpdateData data, @PathVariable Long id) {
        return new AccountPublicData(updateAccountUseCase.execute(id, data));
    }

}
