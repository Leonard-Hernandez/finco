package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.infrastructure.account.dto.AccountTransferData;
import com.finco.finco.usecase.account.TransferAccountUseCase;

import jakarta.validation.Valid;

@RestController
public class TransferAccountController {

    private final TransferAccountUseCase transferAccountUseCase;

    public TransferAccountController(TransferAccountUseCase transferAccountUseCase) {
        this.transferAccountUseCase = transferAccountUseCase;
    }

    @PostMapping("/accounts/{id}/transfer")
    @LogExecution()
    public AccountPublicData transfer(@PathVariable Long id, @Valid @RequestBody AccountTransferData data) {
        return new AccountPublicData(transferAccountUseCase.execute(id, data));
    }

}
