package com.finco.finco.usecase.transaction;

import java.util.List;

import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;

public class GetCategoriesByUserUseCase {

    private final TransactionGateway transactionGateway;
    private final AuthGateway authGateway;

    public GetCategoriesByUserUseCase(TransactionGateway transactionGateway, AuthGateway authGateway) {
        this.transactionGateway = transactionGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public List<String> execute(Long userId) {
        authGateway.verifyOwnershipOrAdmin(userId);
        return transactionGateway.findAllCategoriesByUserId(userId);
    }

}
