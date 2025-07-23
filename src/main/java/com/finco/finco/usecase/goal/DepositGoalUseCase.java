package com.finco.finco.usecase.goal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.finco.finco.entity.account.gateway.AccountGateway;
import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.annotation.LogExecution;
import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import com.finco.finco.entity.goal.gateway.GoalGateway;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.goalAccountBalance.gateway.GoalAccountBalanceGateway;
import com.finco.finco.entity.goalAccountBalance.model.GoalAccountBalance;
import com.finco.finco.entity.exception.InsufficientBalanceException;
import com.finco.finco.entity.security.exception.AccessDeniedBusinessException;
import com.finco.finco.entity.security.gateway.AuthGateway;
import com.finco.finco.entity.transaction.gateway.TransactionGateway;
import com.finco.finco.entity.transaction.model.Transaction;
import com.finco.finco.entity.transaction.model.TransactionType;
import com.finco.finco.usecase.goal.dto.IGoalTransactionData;

public class DepositGoalUseCase {

    private final GoalGateway goalGateway;
    private final GoalAccountBalanceGateway goalAccountBalanceGateway;
    private final AccountGateway accountGateway;
    private final TransactionGateway transactionGateway;
    private final AuthGateway authGateway;

    public DepositGoalUseCase(GoalGateway goalGateway, GoalAccountBalanceGateway goalAccountBalanceGateway,
            TransactionGateway transactionGateway, AuthGateway authGateway, AccountGateway accountGateway) {
        this.goalGateway = goalGateway;
        this.goalAccountBalanceGateway = goalAccountBalanceGateway;
        this.accountGateway = accountGateway;
        this.transactionGateway = transactionGateway;
        this.authGateway = authGateway;
    }

    @TransactionalDomainAnnotation
    @LogExecution
    public Goal execute(Long goalId, IGoalTransactionData data) {

        // validate ownership
        Goal goal = goalGateway.findById(goalId).orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(goal.getUser().getId());

        Account account = accountGateway.findById(data.accountId())
                .orElseThrow(AccessDeniedBusinessException::new);
        authGateway.verifyOwnershipOrAdmin(account.getUser().getId());

        // validate balance
        Optional<GoalAccountBalance> goalAccountBalance = goalAccountBalanceGateway.findAllByGoalId(goalId).stream()
                .filter(goalBalance -> goalBalance.getAccount().getId().equals(data.accountId()))
                .findFirst();

        BigDecimal totalBalanceInGoalsByAccount = goalAccountBalanceGateway
                .getTotalBalanceInGoalsByAccount(account.getId());
        if (totalBalanceInGoalsByAccount.add(data.amount()).compareTo(account.getBalance()) > 0) {
            throw new InsufficientBalanceException();
        }

        // update goal account balance
        if (goalAccountBalance.isPresent()) {
            goalAccountBalance.get().deposit(data.amount());
            goalAccountBalanceGateway.update(goalAccountBalance.get());
        } else {

            GoalAccountBalance newGoalAccountBalance = new GoalAccountBalance();
            newGoalAccountBalance.setGoal(goal);
            newGoalAccountBalance.setAccount(account);
            newGoalAccountBalance.setBalance(data.amount());
            newGoalAccountBalance.setLastUpdated(LocalDateTime.now());
            newGoalAccountBalance.setCreatedAt(LocalDateTime.now());
            goalAccountBalanceGateway.create(newGoalAccountBalance);
        }
        goal.setGoalAccountBalances(goalAccountBalanceGateway.findAllByGoalId(goalId));

        Transaction transaction = new Transaction();
        transaction.setUser(account.getUser());
        transaction.setAccount(account);
        transaction.setDate(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT_GOAL);
        transaction.setGoal(goal);
        transaction.setAmount(data.amount());
        transaction.setCategory(data.category());
        transaction.setDescription(data.description());

        transactionGateway.create(transaction);

        return goalGateway.update(goal);

    }

}
