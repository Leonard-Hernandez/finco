package com.finco.finco.infrastructure.account.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.infrastructure.account.dto.AccountPublicData;
import com.finco.finco.usecase.account.GetAllAccountUseCase;
import static com.finco.finco.infrastructure.config.db.mapper.PageMapper.*;

@RestController
public class GetAllAccountController {

    private final GetAllAccountUseCase getAllAccountUseCase;

    public GetAllAccountController(GetAllAccountUseCase getAllAccountUseCase) {
        this.getAllAccountUseCase = getAllAccountUseCase;
    }

    @GetMapping("/admin/accounts")
    public Page<AccountPublicData> getAllAccounts(@PageableDefault(page = 0, size = 20, sort = "name") Pageable pageable) {
        PageRequest domainPageRequest = toPageRequest(pageable);

        PagedResult<Account> accountsPagedResult = getAllAccountUseCase.execute(domainPageRequest);

        Page<AccountPublicData> responsePage = toPage(accountsPagedResult, pageable).map(AccountPublicData::new);

        return responsePage;
    }

}
