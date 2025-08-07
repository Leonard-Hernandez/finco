package com.finco.finco.usecase.transaction;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;

public class GetTransactionUseCase {

    private final TransactionGateway transactionGateway;
    private final AuthGateway authGateway;

    public GetTransactionUseCase(TransactionGateway transactionGateway, AuthGateway authGateway) {
        this.transactionGateway = transactionGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public Transaction execute(Long id) {
        Transaction transaction = transactionGateway.findById(id).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(transaction.getUser().getId());
        return transaction;
    }

}
