package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.exception.AccountNotFoundException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public GetAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    public Account execute(Long id) {
        authGateway.verifyOwnershipOrAdmin(id);
        return accountGateway.findById(id).orElseThrow(AccountNotFoundException::new);
    }

}
