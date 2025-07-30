package com.finco.finco.usecase.account;

import java.math.BigDecimal;

import com.finco.finco.entity.account.exception.CannotDeactivateDefaultAccountException;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.goal.exception.BalanceInGoalException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class DeleteAccountUseCase {

    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public DeleteAccountUseCase(AccountGateway accountGateway, AuthGateway authGateway) {
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @LogExecution(logReturnValue = false, logArguments = false)
    public Account execute(Long accountId) {

        Account account = accountGateway.findById(accountId).orElseThrow(AccessDeniedBusinessException::new);

        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());

        if (accountGateway.getTotalBalanceInGoalsByAccount(accountId).compareTo(BigDecimal.ZERO) > 0) {
            throw new BalanceInGoalException();
        }

        if (account.isDefault()) {
            throw new CannotDeactivateDefaultAccountException(); 
        }

        return accountGateway.delete(account);
    }

}
