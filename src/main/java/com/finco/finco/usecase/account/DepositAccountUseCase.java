package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.exception.AccountNotFoundException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.usecase.account.dto.IAccountTransactionData;

public class DepositAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public DepositAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    public Account execute(Long id, IAccountTransactionData data) {
        Account account = accountGateway.findById(id).orElseThrow(AccountNotFoundException::new);
        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());
        account.deposit(data.amount());
        return accountGateway.update(account);
    }

}
