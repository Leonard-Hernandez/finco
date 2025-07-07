package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.account.model.AccountType;

@RestController
public class GetAccountTypesController {

    @GetMapping("/accounts/types")
    public AccountType[] getAccountTypes() {
        return AccountType.values();
    }

}
