package com.finco.finco.infrastructure.user.controller;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.infrastructure.user.dto.UserPublicData;
import com.finco.finco.usecase.user.GetAllUserUseCase;

@RestController
public class GetAllUserController {

    private final GetAllUserUseCase findAllUserUseCase;

    public GetAllUserController(GetAllUserUseCase findAllUserUseCase) {
        this.findAllUserUseCase = findAllUserUseCase;
    }

    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)
    private Page<UserPublicData> findAllUsers(@PageableDefault(page = 0, size = 20, sort = "name") Pageable pageable) {
        PageRequest domainPageRequest = new PageRequest(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort().isSorted() ? pageable.getSort().iterator().next().getProperty() : null,
            pageable.getSort().isSorted() ? pageable.getSort().iterator().next().getDirection().name().toLowerCase() : null
        );

        PagedResult<User> usersPagedResult = findAllUserUseCase.execute(domainPageRequest);

        Page<UserPublicData> responsePage = new org.springframework.data.domain.PageImpl<>(
            usersPagedResult.getContent().stream().map(UserPublicData::new).collect(Collectors.toList()),
            pageable,
            usersPagedResult.getTotalElements()
        );

        return responsePage;
    }

}
