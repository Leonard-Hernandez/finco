package com.finco.finco.infrastructure.account.controller;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountUpdateData;
import com.finco.finco.usecase.account.UpdateAccountUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
public class UpdateAccountController {

    private UpdateAccountUseCase updateAccountUseCase;

    public UpdateAccountController(UpdateAccountUseCase updateAccountUseCase) {
        this.updateAccountUseCase = updateAccountUseCase;
    }

    @PutMapping("/account/{id}")
    @Validated
    @ResponseStatus(HttpStatus.OK)
    public AccountPublicData updateAccount(@PathVariable Long id, @Valid @RequestBody AccountUpdateData data) {
        return new AccountPublicData(updateAccountUseCase.execute(id, data));
    }

}
