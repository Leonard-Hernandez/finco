package com.finco.finco.entity.transaction.model;

import java.time.LocalDateTime;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.user.model.User;

public class Transaction {

    private Long id;
    private User user;
    private Account account;
    private TransactionType type;
    private Long amount;
    private LocalDateTime date;
    private String description;
    private String category;
    private Goal goal;
    private Account transferAccount;

    public Transaction() {

    }

    public Transaction(Long id, User user, Account account, TransactionType type, Long amount, LocalDateTime date,
            String description, String category, Goal goal, Account transferAccount) {
        this.id = id;
        this.user = user;
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
        this.goal = goal;
        this.transferAccount = transferAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Account getTransferAccount() {
        return transferAccount;
    }

    public void setTransferAccount(Account transferAccount) {
        this.transferAccount = transferAccount;
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
        Transaction other = (Transaction) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}
