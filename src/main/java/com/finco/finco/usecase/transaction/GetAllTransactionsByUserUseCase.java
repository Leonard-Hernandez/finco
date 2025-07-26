package com.finco.finco.usecase.transaction;

import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetAllTransactionsByUserUseCase {

    private final TransactionGateway transactionGateway;
    private final AuthGateway authGateway;

    public GetAllTransactionsByUserUseCase(TransactionGateway transactionGateway, AuthGateway authGateway) {
        this.transactionGateway = transactionGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public PagedResult<Transaction> execute(PageRequest page, Long userId) {
        authGateway.verifyOwnershipOrAdmin(userId);
        return transactionGateway.findAllByUserId(userId, page);
    }

}
