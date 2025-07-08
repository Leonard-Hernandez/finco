package com.finco.finco.usecase.goal;

import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goalAccountBalance.gateway.GoalAccountBalanceGateway;
import com.finco.finco.entity.security.gateway.AuthGateway;

public class DepositGoalUseCase {

    private final GoalGateway goalGateway;
    private final GoalAccountBalanceGateway goalAccountBalanceGateway;
    private final AuthGateway authGateway;

    public DepositGoalUseCase(GoalGateway goalGateway, GoalAccountBalanceGateway goalAccountBalanceGateway, AuthGateway authGateway) {
        this.goalGateway = goalGateway;
        this.goalAccountBalanceGateway = goalAccountBalanceGateway;
        this.authGateway = authGateway;
    }

    

}
