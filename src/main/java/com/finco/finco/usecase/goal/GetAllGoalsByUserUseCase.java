package com.finco.finco.usecase.goal;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetAllGoalsByUserUseCase {

    private final GoalGateway goalGateway;
    private final AuthGateway authGateway;

    public GetAllGoalsByUserUseCase(GoalGateway goalGateway, AuthGateway authGateway) {
        this.goalGateway = goalGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public PagedResult<Goal> execute(Long userId, PageRequest pageRequest) {

        authGateway.verifyOwnershipOrAdmin(userId);

        return goalGateway.findAllByUserId(userId, pageRequest);
    }

}
