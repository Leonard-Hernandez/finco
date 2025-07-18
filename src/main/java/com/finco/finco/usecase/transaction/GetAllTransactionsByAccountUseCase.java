package com.finco.finco.usecase.transaction;

import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetAllTransactionsByAccountUseCase {

    private final TransactionGateway transactionGateway;
    private final AccountGateway accountGateway;
    private final AuthGateway authGateway;

    public GetAllTransactionsByAccountUseCase(TransactionGateway transactionGateway, AccountGateway accountGateway,
            AuthGateway authGateway) {
        this.transactionGateway = transactionGateway;
        this.accountGateway = accountGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    public PagedResult<Transaction> execute(PageRequest page, Long accountId) {

        Account account = accountGateway.findById(accountId).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());
        return transactionGateway.findAllByAccountId(accountId, page);
    }

}
