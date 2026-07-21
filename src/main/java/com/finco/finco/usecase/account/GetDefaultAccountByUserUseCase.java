package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.exception.DefaultAccountNotFoundException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetDefaultAccountByUserUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public GetDefaultAccountByUserUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public Account execute() {
        return accountGateway.findDefaultByUserId(authGateway.getAuthenticatedUserId())
                .orElseThrow(DefaultAccountNotFoundException::new);
    }

}
