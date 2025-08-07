package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.pagination.filter.IAccountFilterData;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetAllAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public GetAllAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public PagedResult<Account> execute(PageRequest page, IAccountFilterData accountFilterData) {

        if (!authGateway.isAuthenticatedUserInRole("ADMIN")) {
            throw new AccessDeniedBusinessException();
        }

        return accountGateway.findByFilterData(page, accountFilterData);
    }

}
