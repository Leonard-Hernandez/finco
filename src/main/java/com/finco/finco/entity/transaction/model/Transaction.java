package com.finco.finco.entity.transaction.model;

import java.time.LocalDateTime;

public class Transaction {

    private Long id;
    private Long userId;
    private Long account_id;
    private TransactionType type;
    private Long amount;
    private LocalDateTime date;
    private String Description;
    private String category;
    private Long goalId;
    private Long transferAccountId;

    public Transaction() {

    }

    public Transaction(Long id, Long userId, Long account_id, TransactionType type, Long amount, LocalDateTime date,
            String description, String category, Long goalId, Long transferAccountId) {
        this.id = id;
        this.userId = userId;
        this.account_id = account_id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        Description = description;
        this.category = category;
        this.goalId = goalId;
        this.transferAccountId = transferAccountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
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
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    public Long getTransferAccountId() {
        return transferAccountId;
    }

    public void setTransferAccountId(Long transferAccountId) {
        this.transferAccountId = transferAccountId;
    }

}
