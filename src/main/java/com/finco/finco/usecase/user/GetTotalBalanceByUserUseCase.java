package com.finco.finco.usecase.user;

import java.math.BigDecimal;
import java.util.List;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetTotalBalanceByUserUseCase {

    private final AuthGateway authGateway;
    private final AccountGateway accountGateway;

    public GetTotalBalanceByUserUseCase(AuthGateway authGateway, AccountGateway accountGateway) {
        this.authGateway = authGateway;
        this.accountGateway = accountGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public BigDecimal execute(Long userId) {
        authGateway.verifyOwnershipOrAdmin(userId);

        BigDecimal total = BigDecimal.ZERO;

        List<Account> accounts = accountGateway.findAllByUser(userId);

        for (Account account : accounts) {
            BigDecimal balanceInGoals = accountGateway.getTotalBalanceInGoalsByAccount(account.getId());
            total = total.add(account.getBalance().subtract(balanceInGoals));
        }

        return total;
    }

}
