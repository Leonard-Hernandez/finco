package com.finco.finco.usecase.transaction;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetAllTransactionsByGoalsUseCase {

    private final TransactionGateway transactionGateway;
    private final GoalGateway goalGateway;
    private final AuthGateway authGateway;

    public GetAllTransactionsByGoalsUseCase(TransactionGateway transactionGateway, GoalGateway goalGateway,
            AuthGateway authGateway) {
        this.transactionGateway = transactionGateway;
        this.goalGateway = goalGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public PagedResult<Transaction> execute(PageRequest page, Long goalId) {

        Goal goal = goalGateway.findById(goalId).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(goal.getUser().getId());
        return transactionGateway.findAllByGoalId(goalId, page);
    }

}
