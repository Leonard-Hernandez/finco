package com.finco.finco.infrastructure.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.account.model.CurrencyEnum;

@RestController
public class GetCurrenciesController {

    @GetMapping("/accounts/currencies")
    public CurrencyEnum[] getCurrencies() {
        return CurrencyEnum.values();
    }

}
