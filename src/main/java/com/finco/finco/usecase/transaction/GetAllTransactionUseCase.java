package com.finco.finco.usecase.transaction;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.pagination.filter.ITransactionFilterData;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;

public class GetAllTransactionUseCase {

    private final TransactionGateway transactionGateway;
    private final AuthGateway authGateway;

    public GetAllTransactionUseCase(TransactionGateway transactionGateway, AuthGateway authGateway) {
        this.transactionGateway = transactionGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public PagedResult<Transaction> execute(PageRequest page, ITransactionFilterData filterData) {
        if (!authGateway.isAuthenticatedUserInRole("ADMIN")) {
            throw new AccessDeniedBusinessException();
        }
        return transactionGateway.findAllByFilterData(filterData, page);
    }

}
