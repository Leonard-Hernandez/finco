package com.finco.finco.infrastructure.account.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.usecase.account.GetAllAccountsByUserUseCase;

import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
public class GetAllAccountsByUserController {

    private final GetAllAccountsByUserUseCase getAllAccountsByUserUseCase;

    public GetAllAccountsByUserController(GetAllAccountsByUserUseCase getAllAccountsByUserUseCase) {
        this.getAllAccountsByUserUseCase = getAllAccountsByUserUseCase;
    }

    @GetMapping("/users/{userId}/accounts")
    @LogExecution()
    public Page<AccountPublicData> getAllAccountsByUser(@PageableDefault(page = 0, size = 20, sort = "name") Pageable pageable, @PathVariable Long userId) {
        PageRequest domainPageRequest = toPageRequest(pageable);
        PagedResult<Account> accountsPagedResult = getAllAccountsByUserUseCase.execute(domainPageRequest, userId);

        Page<AccountPublicData> responsePage = toPage(accountsPagedResult, pageable).map(AccountPublicData::new);

        return responsePage;
    }

}
