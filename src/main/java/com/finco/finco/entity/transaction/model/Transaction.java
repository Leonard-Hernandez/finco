package com.finco.finco.entity.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.finco.finco.entity.account.model.Account;
import com.finco.finco.entity.exception.AmountMustBeGreaterThanZeroException;
import com.finco.finco.entity.goal.model.Goal;
import com.finco.finco.entity.user.model.User;

public class Transaction {

    private Long id;
    private User user;
    private Account account;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal fee;
    private LocalDateTime date;
    private String description;
    private String category;
    private Goal goal;
    private Account transferAccount;
    private BigDecimal exchangeRate;

    public Transaction() {

    }

    public Transaction(Long id, User user, Account account, TransactionType type, BigDecimal amount, BigDecimal fee,
            LocalDateTime date, String description, String category, Goal goal, Account transferAccount,
            BigDecimal exchangeRate) {
        this.id = id;
        this.user = user;
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.fee = fee;
        this.date = date;
        this.description = description;
        this.category = category;
        this.goal = goal;
        this.transferAccount = transferAccount;
        this.exchangeRate = exchangeRate;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountMustBeGreaterThanZeroException();
        }
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
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

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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
