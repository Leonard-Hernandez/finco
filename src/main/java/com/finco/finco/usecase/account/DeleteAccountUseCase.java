package com.finco.finco.usecase.account;

import com.finco.finco.entity.account.exception.CannotDeactivateDefaultAccountException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class DeleteAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public DeleteAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @LogExecution
    public Account execute(Long accountId) {

        Account account = accountGateway.findById(accountId).orElseThrow(AccessDeniedBusinessException::new);

        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());

        if (account.isDefault()) {
            throw new CannotDeactivateDefaultAccountException(); 
        }

        return accountGateway.delete(account);
    }

}
