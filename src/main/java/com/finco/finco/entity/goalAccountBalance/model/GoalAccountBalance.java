package com.finco.finco.entity.goalAccountBalance.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.goal.model.Goal;

public class GoalAccountBalance {

    private Long id;
    private Goal goal;
    private Account account;
    private BigDecimal balance;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;

    public GoalAccountBalance() {
    };

    public GoalAccountBalance(Long id, Goal goal, Account account, BigDecimal balance, LocalDateTime lastUpdated,
            LocalDateTime createdAt) {
        this.id = id;
        this.goal = goal;
        this.account = account;
        this.balance = balance;
        this.lastUpdated = lastUpdated;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.lastUpdated = LocalDateTime.now();
    }

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        this.lastUpdated = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GoalAccountBalance other = (GoalAccountBalance) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
