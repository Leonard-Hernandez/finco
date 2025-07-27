package com.finco.finco.infrastructure.user.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.usecase.user.GetTotalBalanceByUserUseCase;

@RestController
public class GetTotalBalanceByUserController {

    private final GetTotalBalanceByUserUseCase getTotalBalanceByUser;

    public GetTotalBalanceByUserController(GetTotalBalanceByUserUseCase getTotalBalanceByUser) {
        this.getTotalBalanceByUser = getTotalBalanceByUser;
    }

    @GetMapping("/users/{id}/total-balance")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution()
    public BigDecimal getTotalBalanceByUser(@PathVariable Long id) {
        return getTotalBalanceByUser.execute(id);
    }

}
