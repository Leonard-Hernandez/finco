package com.finco.finco.usecase.goal;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.pagination.PageRequest;
import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.pagination.filter.IGoalFilterData;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class GetAllGoalsUseCase {

    private final GoalGateway goalGateway;
    private final AuthGateway authGateway;

    public GetAllGoalsUseCase(GoalGateway goalGateway, AuthGateway authGateway) {
        this.goalGateway = goalGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation(readOnly = true)
    @LogExecution(logReturnValue = false, logArguments = false)
    public PagedResult<Goal> execute(PageRequest pageRequest, IGoalFilterData goalFilterData) {

        if (!authGateway.isAuthenticatedUserInRole("ADMIN")) {
            throw new AccessDeniedBusinessException();
        }

        return goalGateway.findAllByFilterData(goalFilterData, pageRequest);

    }

}
