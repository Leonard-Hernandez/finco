package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetAllAccountsByUserUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public GetAllAccountsByUserUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }
    
    @TransactionalDomainAnnotation(readOnly = true)
    public PagedResult<Account> execute(PageRequest page, Long userId) {
        
        authGateway.verifyOwnershipOrAdmin(userId);

        return accountGateway.findAllByUser(page, userId);
    }

}
