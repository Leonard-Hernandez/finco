package com.finco.finco.usecase.goal;

import java.time.LocalDateTime;
import java.util.Optional;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.usecase.goal.dto.IGoalDepositData;

public class DepositGoalUseCase {

    private final GoalGateway goalGateway;
    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;
    private final AuthGateway authGateway;

    public DepositGoalUseCase(GoalGateway goalGateway, AccountGateway accountGateway,
            TransactionGateway transactionGateway, AuthGateway authGateway) {
        this.goalGateway = goalGateway;
        this.accountGateway = accountGateway;
        this.transactionGateway = transactionGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    public Goal execute(Long goalId, IGoalDepositData goalDepositData) {

        Goal goal = goalGateway.findById(goalId).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(goal.getUser().getId());

        Account account = accountGateway.findById(goalDepositData.accountId())
                .orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());

        Optional<GoalAccountBalance> goalAccountBalance = goal.getGoalAccountBalances().stream()
                .filter(goalBalance -> goalBalance.getAccount().getId().equals(goalDepositData.accountId()))
                .findFirst();

        if (goalAccountBalance.isPresent()) {
            goalAccountBalance.get().deposit(goalDepositData.amount());
        } else {
            GoalAccountBalance newGoalAccountBalance = new GoalAccountBalance();
            newGoalAccountBalance.setGoal(goal);
            newGoalAccountBalance.setAccount(account);
            newGoalAccountBalance.setBalance(goalDepositData.amount());
            newGoalAccountBalance.setLastUpdated(LocalDateTime.now());
            newGoalAccountBalance.setCreatedAt(LocalDateTime.now());
            goal.getGoalAccountBalances().add(newGoalAccountBalance);
        }

        Transaction transaction = new Transaction();
        transaction.setUser(account.getUser());
        transaction.setAccount(account);
        transaction.setDate(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT_GOAL);
        transaction.setGoal(goal);
        transaction.setAmount(goalDepositData.amount());
        transaction.setCategory(goalDepositData.category());
        transaction.setDescription(goalDepositData.description());

        transactionGateway.create(transaction);

        accountGateway.update(account);

        return goalGateway.update(goal);

    }

}
