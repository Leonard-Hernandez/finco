package com.finco.finco.usecase.goal;

import java.time.LocalDateTime;

import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.user.exception.UserNotFoundException;
import com.finco.finco.entity.user.gateway.UserGateway;
import com.finco.finco.entity.user.model.User;
import com.finco.finco.usecase.goal.dto.IGoalRegistrationData;

public class CreateGoalUseCase {

    private final GoalGateway goalGateway;
    private final UserGateway userGateway;
    private final AuthGateway authGateway;

    public CreateGoalUseCase(GoalGateway goalGateway, UserGateway userGateway, AuthGateway authGateway) {
        this.goalGateway = goalGateway;
        this.userGateway = userGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    public Goal execute(Long goalId, IGoalRegistrationData data) {

        authGateway.verifyOwnershipOrAdmin(data.userId());

        User user = userGateway.findById(data.userId()).orElseThrow(UserNotFoundException::new);

        Goal goal = new Goal();
        goal.setUser(user);
        goal.setName(data.name());
        goal.setTargetAmount(data.targetAmount());
        goal.setDeadLine(data.deadLine());
        goal.setDescription(data.description());
        goal.setCreationDate(LocalDateTime.now());
        goal.setEnable(true);

        return goalGateway.create(goal);
    }

}
