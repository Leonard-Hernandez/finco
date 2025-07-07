package com.finco.finco.infrastructure.transaction.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.usecase.transaction.GetCategoriesByUserUseCase;

@RestController
public class GetCategoriesByUserController {

    private final GetCategoriesByUserUseCase getCategoriesByUserUseCase;

    public GetCategoriesByUserController(GetCategoriesByUserUseCase getCategoriesByUserUseCase) {
        this.getCategoriesByUserUseCase = getCategoriesByUserUseCase;
    }

    @GetMapping("/transactions/categories/{userId}")
    public List<String> getCategoriesByUser(@PathVariable Long userId) {
        return getCategoriesByUserUseCase.execute(userId);
    }

}
