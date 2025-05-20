package com.finco.finco.entity.transaction.model;

import java.time.LocalDateTime;

public class Transaction {

    private Long id;
    private Long userId;
    private Long account_id;
    private TransactionType type;
    private Long amount;
    private LocalDateTime date;
    private String description;
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
        this.description = description;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }
    
}
