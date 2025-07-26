package com.finco.finco.usecase.goal;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class DeleteGoalUseCase {

    private final GoalGateway goalGateway;
    private final AuthGateway authGateway;

    public DeleteGoalUseCase(GoalGateway goalGateway, AuthGateway authGateway) {
        this.goalGateway = goalGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    @LogExecution(logReturnValue = false, logArguments = false)
    public Goal execute(Long goalId) {

        Goal goal = goalGateway.findById(goalId).orElseThrow(AccessDeniedBusinessException::new);

        authGateway.verifyOwnershipOrAdmin(goal.getUser().getId());

        return goalGateway.delete(goal);

    }

}
