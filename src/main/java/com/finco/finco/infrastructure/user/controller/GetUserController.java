package com.finco.finco.infrastructure.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.usecase.user.GetUserUseCase;

@RestController
public class GetUserController {

    private final GetUserUseCase getUserUserCase;

    public GetUserController(GetUserUseCase getUserController){
        this.getUserUserCase = getUserController;
    }

    @GetMapping("/user{id}")
    public UserPublicData getUser(@PathVariable long id) {

        return new UserPublicData(getUserUserCase.execute(id));

    }

}
