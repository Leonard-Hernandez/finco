package com.finco.finco.usecase.goal;

import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.usecase.goal.dto.IGoalUpdateData;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class UpdateGoalUseCase {

    private final GoalGateway goalGateway;
    private final AuthGateway authGateway;

    public UpdateGoalUseCase(GoalGateway goalGateway, AuthGateway authGateway) {
        this.goalGateway = goalGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    @LogExecution
    public Goal execute(Long goalId, IGoalUpdateData data) {

        Goal goal = goalGateway.findById(goalId).orElseThrow(AccessDeniedBusinessException::new);

        authGateway.verifyOwnershipOrAdmin(goal.getUser().getId());

        if (data.name() != null) {
            goal.setName(data.name());
        }

        if (data.targetAmount() != null) {
            goal.setTargetAmount(data.targetAmount());
        }

        if (data.deadLine() != null) {
            goal.setDeadLine(data.deadLine());
        }

        if (data.description() != null) {
            goal.setDescription(data.description());
        }

        if (data.enable() != null) {
            goal.setEnable(data.enable());
        }

        return goalGateway.update(goal);
    }

}
