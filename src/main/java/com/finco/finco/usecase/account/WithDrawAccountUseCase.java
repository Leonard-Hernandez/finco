package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.exception.AccountNotFoundException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.infrastructure.account.dto.AccountTransactionData;

public class WithDrawAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public WithDrawAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    public Account execute(Long id, AccountTransactionData data) {
        Account account = accountGateway.findById(id).orElseThrow(AccountNotFoundException::new);
        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());
        account.withdraw(data.amount());
        return accountGateway.update(account);
    }

}
